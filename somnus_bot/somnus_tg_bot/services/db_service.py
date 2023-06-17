from database.database import create_table, insert_data, get_data, get_all_tg_users
from lexicon.lexicon_ru import LEXICON_POSSIBLE_RESPONSE
from psycopg2 import OperationalError
from psycopg2.errors import UniqueViolation


def create_user_table():
    try:
        create_table()
    except OperationalError as e:
        raise OperationalError

def insert_new_user(tg_id:int, somnus_id:int, authorUsername:str) -> int:
    try:
        insert_data(tg_id, somnus_id, authorUsername)
        return LEXICON_POSSIBLE_RESPONSE['OK']
    except OperationalError as e:
        return LEXICON_POSSIBLE_RESPONSE['CONNECTION_ERROR_TG']

    except UniqueViolation as e:
        return LEXICON_POSSIBLE_RESPONSE['UNIQUE_VIOLATION']


def get_user_somnus_id(tg_id:int) -> int:
    try:
        somnus_id, somnus_author_username = get_data(tg_id)
        return somnus_id
    except OperationalError as e:
        return LEXICON_POSSIBLE_RESPONSE['CONNECTION_ERROR_TG']

def get_all_users() -> list[int]|int:

    try:
        all_users_ids = get_all_tg_users()
        return all_users_ids
    except OperationalError as e:
        return LEXICON_POSSIBLE_RESPONSE['CONNECTION_ERROR_TG']
