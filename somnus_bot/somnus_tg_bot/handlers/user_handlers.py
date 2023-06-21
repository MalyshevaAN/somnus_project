from copy import deepcopy
from aiogram import Router, Bot, F
from aiogram.filters import Command, CommandStart, Text, StateFilter
from aiogram.fsm.context import FSMContext
from aiogram.fsm.state import default_state
from aiogram.types import CallbackQuery, Message
from filters.message_filter import IsTextMessage
from services.image_service import get_add_dream_image


from keyboards.keyboard_utils import create_keyboard
from lexicon.lexicon_ru import LEXICON, LEXICON_COMMANDS, LEXICON_COMMANDS_FIRST
from services.image_service import get_add_dream_image
from services.dream_service import get_random_dream, add_my_dream
from states.user_states import FSMAddDream, FSMConnectAccounts
from aiogram import Router, Bot
from config_data.config import Config, load_config
from keyboards.keyboard_commands import set_first_menu, set_main_menu_commands
from exceptions import exceptions
from keyboards.keyboard_utils import create_keyboard
from requests.exceptions import ConnectionError


config: Config = load_config('somnus_tg_bot/.env')
bot: Bot = Bot(config.tg_bot.token)

router:Router = Router()


@router.message(CommandStart(), StateFilter(default_state))
async def process_start_command(message: Message):
    await message.answer(f'Привет, {message.from_user.first_name}!' + " " + LEXICON_COMMANDS_FIRST['/start'])
    await set_first_menu(bot=bot)

@router.message(CommandStart(), StateFilter(FSMConnectAccounts.connected))
async def process_start_command(message: Message):
    await message.answer(f'Привет, {message.from_user.first_name}!' + " " + LEXICON_COMMANDS['/start'])
    await set_main_menu_commands(bot=bot)


@router.message(Command(commands='help'), StateFilter(default_state))
async def process_help_command(message: Message):
    await message.answer(LEXICON_COMMANDS_FIRST['/help'])


@router.message(Command(commands='help'), StateFilter(FSMConnectAccounts.connected))
async def process_help_command(message: Message):
    await message.answer(LEXICON_COMMANDS['/help'])


@router.message(Command(commands='add'), StateFilter(FSMConnectAccounts.connected))
async def process_add_command(message: Message, state: FSMContext):
    await message.answer_photo(photo=get_add_dream_image(), caption=LEXICON_COMMANDS['/add'])
    await state.set_state(FSMAddDream.add_dream)

@router.message(Command(commands='add'), ~StateFilter(FSMConnectAccounts.connected))
async def process_add_command(message: Message, state: FSMContext):
    await message.answer(LEXICON['not_connected'])

@router.message(StateFilter(FSMAddDream.add_dream), IsTextMessage())
async def process_add_dream(message:Message, state: FSMContext):
    try:
        add_my_dream(message.text, message.from_user.id)
        await message.answer(LEXICON['dream_is_added'], reply_markup=create_keyboard('add_one_more', 'add_no_more'))
        await state.set_state(FSMAddDream.add_one_more)
    except exceptions.DreamIsNotCreated:
        await message.answer(LEXICON['bad_request'])
        await state.clear()
    except exceptions.ConnectionErrorMicro:
        await message.answer(LEXICON['something_wrong_with_dream_service'])
        await state.clear()
    except ConnectionError:
        await message.answer(LEXICON['something_wrong_with_somnus_tg_db'])
        await state.clear()

@router.callback_query(StateFilter(FSMAddDream.add_one_more), Text(text='add_one_more'))
async def process_add_one_more(callback: CallbackQuery, state:FSMContext):
    await callback.message.answer_photo(photo=get_add_dream_image(), caption=LEXICON_COMMANDS['/add'])
    await callback.message.delete()
    await state.set_state(FSMAddDream.add_dream)

@router.callback_query(StateFilter(FSMAddDream.add_one_more), Text(text='add_no_more'))
async def process_no_add(callback: CallbackQuery, state: FSMContext):
    await callback.message.answer(LEXICON['no_more'])
    await callback.message.delete()
    await state.set_state(FSMConnectAccounts.connected)

@router.message(StateFilter(FSMAddDream.add_one_more))
async def process_message_withount_callback(message:Message, state:FSMContext):
    await message.answer(LEXICON['push_button'], reply_markup=create_keyboard('add_one_more', 'add_no_more'))
    await state.set_state(FSMAddDream.add_one_more)

@router.message(StateFilter(FSMAddDream.add_dream), Command(commands='cancel'))
async def cancel_add_dream(message: Message, state: FSMContext):
    await message.answer(LEXICON['cancel_add_dream'])
    await state.set_state(FSMConnectAccounts.connected)

@router.message(StateFilter(FSMAddDream.add_dream))
async def process_add_dream_incorrect(message: Message, state: FSMContext):
    await message.answer(LEXICON['add_dream_incorrect'])
    await state.set_state(FSMAddDream.add_dream)

@router.message(Command(commands='read'), StateFilter(FSMConnectAccounts.connected))
async def process_add_command(message: Message):
    try:
        dream = get_random_dream()
        await message.answer(LEXICON_COMMANDS['/read'] + dream)
    except exceptions.NotFound:
        await message.answer(LEXICON['no_dreams'])
    except ConnectionError:
        await message.answer(LEXICON['something_wrong_with_dream_service'])

@router.message(Command(commands='read'), ~StateFilter(FSMConnectAccounts.connected))
async def process_add_command(message: Message):
    await message.answer(LEXICON['not_connected'])

@router.callback_query(Text(text='yes'), StateFilter(FSMConnectAccounts.connected))
async def process_yes_callback(callback: CallbackQuery, state: FSMContext):
    if callback.message.caption != LEXICON['want_to_add']:
        await callback.message.edit_caption(caption=LEXICON['want_to_add'])
        await state.set_state(FSMAddDream.add_dream)
    else:
        await callback.message.answer()

@router.callback_query(Text(text='yes'), ~StateFilter(FSMConnectAccounts.connected))
async def process_yes_callback(callback: CallbackQuery, state: FSMContext):
    await callback.message.delete_reply_markup()
    await callback.message.answer(LEXICON['not_connected'])

@router.callback_query(Text(text='no'), StateFilter(FSMConnectAccounts.connected))
async def process_no_answer(callback: CallbackQuery):
    if callback.message.caption != LEXICON['nothing_to_write']:
        await callback.message.edit_caption(caption=LEXICON['nothing_to_write'])
    else:
        await callback.message.answer()

@router.callback_query(Text(text='no'), ~StateFilter(FSMConnectAccounts.connected))
async def process_no_answer(callback: CallbackQuery):
    await callback.message.delete_reply_markup()
    await callback.message.answer(LEXICON['not_connected'])

@router.message(Command(commands='cancel'), StateFilter(FSMConnectAccounts.connected))
async def process_cancel_command(message: Message):
    await message.answer(LEXICON['nothing_to_cancel'])


