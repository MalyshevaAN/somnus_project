from copy import deepcopy
from aiogram import Router, F, Bot
from aiogram.filters import Command, Text, StateFilter
from aiogram.fsm.context import FSMContext
from aiogram.fsm.state import default_state
from aiogram.types import CallbackQuery, Message
from services.db_service import insert_new_user
from keyboards.keyboard_utils import create_keyboard
from lexicon.lexicon_ru import LEXICON, LEXICON_COMMANDS_FIRST
from services.user_service import get_user_by_email
from states.user_states import  FSMConnectAccounts
from aiogram import Router
from keyboards.keyboard_commands import  set_main_menu_commands
from services.generate_code import generate_code
from services.email_service import send_code
from config_data.config import Config, load_config
from keyboards.keyboard_utils import create_keyboard
from filters.commands_filter import CommandFilter
import logging
from requests.exceptions import ConnectionError
import smtplib
from psycopg2 import OperationalError
from psycopg2.errors import UniqueViolation
from exceptions import exceptions


logger = logging.getLogger(__name__)

config: Config = load_config('somnus_tg_bot/.env')
bot: Bot = Bot(config.tg_bot.token)
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

@router.message(CommandFilter(), StateFilter(FSMConnectAccounts.send_email))
async def command_send_mail(message: Message):
    await message.answer(LEXICON['command_send_email'])

@router.message(StateFilter(FSMConnectAccounts.send_email))
async def process_send_email(message: Message, state: FSMContext):
    try:
        user = get_user_by_email(message.text)
        id = user.get('id')
        authorUsername = user['firstName'] + " " + user['lastName']
        code = generate_code()
        try:
            send_code(message.text, code)
            await message.answer(LEXICON['send_check_code'])
            await state.update_data(id=id)
            await state.update_data(code=code)
            await state.update_data(authorUsername = authorUsername)
            await state.set_state(FSMConnectAccounts.check_code)
        except smtplib.SMTPConnectError:
            await message.answer(LEXICON['mail_error'])
            await state.clear()
    except ConnectionError as e:
          await message.answer(LEXICON['something_went_wrong'])
          await state.clear()
    except exceptions.NotFound:
        await message.answer(LEXICON['email_not_exists'], reply_markup=create_keyboard('email_again', 'cancel_connection'))



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
        try:
            insert_new_user(int(message.from_user.id), int(user_info['id']), user_info['authorUsername'])
            await message.answer(LEXICON['accounts_are_connected'])
            await state.set_state(FSMConnectAccounts.connected)
            await state.update_data(code=None, id=None, authorUsername=None)
            await set_main_menu_commands(bot=bot)
        except OperationalError:
            await message.answer(LEXICON['something_wrong_with_somnus_tg_db'])
            await state.clear()
        except UniqueViolation:
            await message.answer(LEXICON['accounts_are_connected'])
            await state.set_state(FSMConnectAccounts.connected)
            await state.update_data(code=None, id=None, authorUsername=None)
            await set_main_menu_commands(bot=bot)


    else:
        await message.answer(LEXICON['wrong_check_code'], reply_markup=create_keyboard('email_again', 'cancel_connection'))
        await state.set_state(FSMConnectAccounts.send_email)

@router.message(CommandFilter(), StateFilter(FSMConnectAccounts.check_code))
async def command_send_code(message: Message):
    await message.answer(LEXICON['command_send_code'])

@router.message(StateFilter(FSMConnectAccounts.check_code))
async def process_check_code(message:Message, state: FSMContext):
    await message.answer(LEXICON['wrong_check_code'], reply_markup=create_keyboard('email_again', 'cancel_connection'))
    await state.set_state(FSMConnectAccounts.send_email)

@router.message(Command(commands='cancel'), StateFilter(default_state))
async def process_default_cancel(message:Message):
    await message.answer(LEXICON['nothing_to_cancel'])