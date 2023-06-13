import psycopg2

import dotenv

import os

dotenv.load_dotenv()

DB_NAME = os.getenv('DB_NAME')

DB_HOST = os.getenv('DB_HOST')

DB_PORT = os.getenv('DB_PORT')

DB_USER = os.getenv('DB_USER')

DB_PASSWORD = os.getenv("DB_PASSWORD")


def get_connection():
    connection = psycopg2.connect(dbname=DB_NAME, host=DB_HOST, user=DB_USER, password=DB_PASSWORD, port=DB_PORT)
    return connection


def use_bd(sql: str):
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute(sql)
    conn.commit()
    cursor.close()
    conn.close()

def create_table() -> None:
    sql_create = '''CREATE TABLE IF NOT EXISTS SOMNUSTG(
        ID SERIAL PRIMARY KEY,
        tg_id BIGINT NOT NUll UNIQUE,
        somnus_id BIGINT NOT NULL UNIQUE
    );
    '''
    use_bd(sql_create)


def insert_data(tg_id:int, somnus_id:int):
    sql_insert = f'INSERT INTO SOMNUSTG (tg_id, somnus_id) VALUES ({tg_id}, {somnus_id})'
    use_bd(sql_insert)


def get_data(tg_id:int) -> int:
    conn = get_connection()
    cursor = conn.cursor()
    sql_get = f'SELECT * FROM SOMNUSTG WHERE tg_id={tg_id}'
    cursor.execute(sql_get)
    bd_id, tg_id_bd, somnus_id = cursor.fetchone()
    cursor.close()
    conn.close()
    return somnus_id

def get_all_tg_users() -> list[int]:
    conn = get_connection()
    cursor = conn.cursor()
    sql_get_all = f'SELECT tg_id FROM SOMNUSTG'
    cursor.execute(sql_get_all)
    tg_ids = []
    for elem in cursor.fetchall():
        tg_ids.append(elem[0])
    print(tg_ids)
    cursor.close()
    conn.close()
