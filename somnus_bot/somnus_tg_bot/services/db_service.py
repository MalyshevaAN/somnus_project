from database.database import create_table, insert_data, get_data, get_all_tg_users
from psycopg2 import OperationalError
from psycopg2.errors import UniqueViolation
import logging
from exceptions import exceptions

logger = logging.getLogger(__name__)
logging.basicConfig(
        level=logging.INFO,
        format='%(filename)s:%(lineno)d #%(levelname)-8s '
               '[%(asctime)s] - %(name)s - %(message)s')


def create_user_table():
    try:
        create_table()
        logger.info("table is created")
    except OperationalError as e:
        logger.error("error while creating table")
        raise OperationalError

def insert_new_user(tg_id:int, somnus_id:int, authorUsername:str) -> int:
    try:
        insert_data(tg_id, somnus_id, authorUsername)
        logger.info("user is created")
    except OperationalError as e:
        logger.error("connection error")
        raise OperationalError

    except UniqueViolation as e:
        logger.error("unique user violation")
        raise UniqueViolation


def get_user_somnus(tg_id:int):
    try:
        somnus_id, somnus_author_username = get_data(tg_id)
        logger.info("info about user is got")
        return (int(somnus_id), somnus_author_username)
    except OperationalError as e:
        logger.error("connection error")
        raise OperationalError

def get_all_users() -> list[int]|int:

    try:
        all_users_ids = get_all_tg_users()
        logger.info('all ids are got')
        return all_users_ids
    except OperationalError as e:
        logger.error("connection error")
        raise OperationalError
