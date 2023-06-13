from copy import deepcopy
from aiogram import Router, F
from aiogram.filters import Command, Text, StateFilter
from aiogram.fsm.context import FSMContext
from aiogram.fsm.state import default_state
from aiogram.types import CallbackQuery, Message
from services.db_service import insert_new_user
from keyboards.keyboard_utils import create_keyboard
from lexicon.lexicon_ru import LEXICON, LEXICON_COMMANDS, LEXICON_COMMANDS_FIRST, LEXICON_POSSIBLE_RESPONSE
from services.user_service import get_id_by_email
from states.user_states import  FSMConnectAccounts
from aiogram import Router
from keyboards.keyboard_commands import  set_main_menu_commands
from services.generate_code import generate_code
from services.email_service import send_code

from keyboards.keyboard_utils import create_keyboard


router: Router = Router()

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
    id = get_id_by_email(message.text)
    if id > 0:
        code = generate_code()
        sended = send_code(message.text, code)
        await message.answer(LEXICON['send_check_code'])
        await state.update_data(id=id)
        await state.update_data(code=code)
        await state.set_state(FSMConnectAccounts.check_code)

    elif id == -1:
        await message.answer(LEXICON['email_not_exists'], reply_markup=create_keyboard('email_again', 'cancel_connection'))
    elif id == -2:
        await message.answer(LEXICON['something_went_wrong'])
        await state.clear()


@router.callback_query(StateFilter(FSMConnectAccounts.send_email), Text(text='email_again'))
async def send_another_email(callback: CallbackQuery, state: FSMContext):
    await callback.message.answer(LEXICON_COMMANDS_FIRST['/connect'])
    await callback.message.delete()
    await state.update_data(code=None, id=None)


@router.callback_query(StateFilter(FSMConnectAccounts.send_email), Text(text='cancel_connection'))
async def cancel_connection(callback: CallbackQuery, state: FSMContext):
    await callback.message.answer(LEXICON['cancel_connection_message'])
    await callback.message.delete()
    await state.clear()


@router.message(StateFilter(FSMConnectAccounts.check_code), lambda x: x.text.isdigit() and len(x.text.replace(' ', '')) == 6)
async def process_check_code(message: Message, state: FSMContext):
    info = await state.get_data()
    if (message.text.replace(' ', '') == info['code']):
        user_info = await state.get_data()
        response_status = insert_new_user(int(message.from_user.id), user_info['id'])
        if response_status == LEXICON_POSSIBLE_RESPONSE['OK']:
            await message.answer(LEXICON['accounts_are_connected'])
            await state.set_state(FSMConnectAccounts.connected)
            await state.update_data(code=None, id=None)
        elif response_status == LEXICON_POSSIBLE_RESPONSE['CONNECTION_ERROR_TG']:
            await message.answer(LEXICON['something_wrong_with_somnus_tg_db'])
            await state.clear()
    else:
        await message.answer(LEXICON['wrong_check_code'], reply_markup=create_keyboard('email_again', 'cancel_connection'))
        await state.set_state(FSMConnectAccounts.send_email)



@router.message(StateFilter(FSMConnectAccounts.check_code))
async def process_check_code(message:Message, state: FSMContext):
    await message.answer(LEXICON['wrong_check_code'], reply_markup=create_keyboard('email_again', 'cancel_connection'))
    await state.set_state(FSMConnectAccounts.send_email)