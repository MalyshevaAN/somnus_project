from copy import deepcopy
from aiogram import Router, Bot, F
from aiogram.filters import Command, CommandStart, Text, StateFilter
from aiogram.fsm.context import FSMContext
from aiogram.fsm.state import default_state
from aiogram.types import CallbackQuery, Message
from database.database import user_db, user_dreams_template, user_connected
from filters.callback_filter import IsAddCallbackData, IsNoCallbackData, IsNoMoreCallbackData, IsYesCallbackData
from services.image_service import get_add_dream_image


from keyboards.keyboard_utils import create_keyboard
from lexicon.lexicon_ru import LEXICON, LEXICON_COMMANDS, LEXICON_COMMANDS_FIRST
from services.image_service import get_add_dream_image, get_good_morning_image
import asyncio
from services.db_service import get_users
from services.dream_service import get_random_dream, add_dream
from services.user_service import email_exists
from states.user_states import FSMAddDream, FSMConnectAccounts
from aiogram import Router, Bot
import dotenv, os
import asyncio
from keyboards.keyboard_commands import set_first_menu, set_main_menu_commands

from keyboards.keyboard_utils import create_keyboard

dotenv.load_dotenv()

bot: Bot = Bot(os.getenv('BOT_TOKEN'))

router:Router = Router()

help_text = LEXICON_COMMANDS_FIRST['/help']

@router.message(CommandStart(), StateFilter(default_state))
async def process_start_command(message: Message):
    await message.answer(f'Привет, {message.from_user.first_name}!' + " " + LEXICON_COMMANDS['/start'])
    if message.from_user.id not in user_db:
        user_db[message.from_user.id] = deepcopy(user_dreams_template)
    await set_first_menu(bot=bot)


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

@router.message(StateFilter(FSMAddDream.add_dream))
async def process_add_dream(message:Message, state: FSMContext):
    add_dream(message.text)
    await message.answer(LEXICON['dream_is_added'], reply_markup=create_keyboard('add_one_more', 'add_no_more'))
    await state.set_state(FSMAddDream.add_one_more)

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
    dream = get_random_dream()
    await message.answer(LEXICON_COMMANDS['/read'] + dream)



@router.message(Command(commands='read'), ~StateFilter(FSMConnectAccounts.connected))
async def process_add_command(message: Message):
    await message.answer(LEXICON['not_connected'])


@router.message(Command(commands='connect'), StateFilter(default_state))
async def process_connect_command(message: Message, state:FSMContext):
    await message.answer(LEXICON_COMMANDS_FIRST['/connect'])
    await state.set_state(FSMConnectAccounts.send_email)


@router.message(Command(commands='connect'), StateFilter(FSMConnectAccounts.connected))
async def process_connect_commnad_connected(message: Message, state: FSMContext):
     await message.answer(LEXICON['already_connected'])


@router.message(Command(commands='cancel'), StateFilter(FSMConnectAccounts.check_code, FSMConnectAccounts.send_email))
async def cancel_connect_accounts(message: Message, state: FSMContext):
    await message.answer(LEXICON['cancel_connection_message'])
    await state.clear()


@router.message(StateFilter(FSMConnectAccounts.send_email))
async def process_send_email(message: Message, state: FSMContext):
    if email_exists(message.text):
        await message.answer(LEXICON['send_check_code'])
        await state.update_data(id=1)
        await state.set_state(FSMConnectAccounts.check_code)
    else:
        await message.answer(LEXICON['email_not_exists'], reply_markup=create_keyboard('email_again', 'cancel_connection'))


@router.callback_query(StateFilter(FSMConnectAccounts.send_email), Text(text='email_again'))
async def send_another_email(callback: CallbackQuery):
    if callback.message.text != LEXICON_COMMANDS_FIRST['/connect']:
        await callback.message.edit_text(LEXICON_COMMANDS_FIRST['/connect'])
        await callback.message.delete_reply_markup()
    else:
        await callback.message.answer()


@router.callback_query(StateFilter(FSMConnectAccounts.send_email), Text(text='cancel_connection'))
async def cancel_connection(callback: CallbackQuery, state: FSMContext):
    await callback.message.answer(LEXICON['cancel_connection_message'])
    await callback.message.delete()
    await state.clear()


@router.message(StateFilter(FSMConnectAccounts.check_code), F.text.isdigit())
async def process_check_code(message: Message, state: FSMContext):
    await message.answer(LEXICON['accounts_are_connected'])
    await state.set_state(FSMConnectAccounts.connected)
    user_id = await state.get_data()
    user_connected[message.from_user.id] = user_id['id']
    print(user_connected)
    await set_main_menu_commands(bot=bot)
    LEXICON_COMMANDS_FIRST['/help'] = LEXICON_COMMANDS['/help']


@router.message(StateFilter(FSMConnectAccounts.check_code))
async def process_check_code(message:Message, state: FSMContext):
    await message.answer(LEXICON['wrong_check_code'], reply_markup=create_keyboard('email_again', 'cancel_connection'))
    await state.set_state(FSMConnectAccounts.send_email)


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


@router.message()
async def check(message: Message):
    await message.answer(text="Не понимаю тебя :(\nВоспользуйся командой /help, чтобы посмотреть, какие возможности есть у бота")
