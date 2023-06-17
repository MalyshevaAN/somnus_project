import psycopg2
from  config_data.config import load_config, Config

config:Config = load_config('somnus_tg_bot/.env')

DB_NAME = config.db.db_name

DB_HOST = config.db.db_host

DB_PORT = config.db.db_port

DB_USER = config.db.db_user

DB_PASSWORD = config.db.db_password


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
    sql_create = '''CREATE TABLE IF NOT EXISTS SOMNUSUSERTG(
        ID SERIAL PRIMARY KEY,
        tg_id BIGINT NOT NUll UNIQUE,
        somnus_id BIGINT NOT NULL UNIQUE,
        author_username VARCHAR(255) NOT NULL
    );
    '''
    use_bd(sql_create)


def insert_data(tg_id:int, somnus_id:int, authorUsername:str):
    sql_insert = f"INSERT INTO SOMNUSUSERTG (tg_id, somnus_id, author_username) VALUES ({tg_id}, {somnus_id}, '{authorUsername}')"
    use_bd(sql_insert)


def get_data(tg_id:int) -> tuple:
    conn = get_connection()
    cursor = conn.cursor()
    sql_get = f'SELECT * FROM SOMNUSUSERTG WHERE tg_id={tg_id}'
    cursor.execute(sql_get)
    bd_id, tg_id_bd, somnus_id, authorUsername = cursor.fetchone()
    cursor.close()
    conn.close()
    return (somnus_id, authorUsername)

def get_all_tg_users() -> list[int]:
    conn = get_connection()
    cursor = conn.cursor()
    sql_get_all = f'SELECT tg_id FROM SOMNUSUSERTG'
    cursor.execute(sql_get_all)
    tg_ids = []
    for elem in cursor.fetchall():
        tg_ids.append(elem[0])
    print(tg_ids)
    cursor.close()
    conn.close()
