from dataclasses import dataclass
import os
import dotenv

dotenv.load_dotenv()


# @dataclass
# class DatabaseConfig:
#     database: str
#     db_host: str
#     db_user: str
#     db_password: str

@dataclass
class TgBot:
    token: str

@dataclass
class RedisConfig:
    host: str

@dataclass
class DatabaseConfig:
    db_name: str
    db_host:str
    db_user: str
    db_password: str
    db_port: str


@dataclass
class Connections:
    email_endpoint: str
    random_dream_endpoint: str
    post_dream_endpoint: str


@dataclass
class Mail:
    email_user: str
    email_password: str
    email_server: str


@dataclass
class Config:
    tg_bot: TgBot
    db: DatabaseConfig
    redis: RedisConfig
    connections: Connections
    mail: Mail


def load_config(path:str | None) -> Config:
    return Config(tg_bot=TgBot(token=os.getenv("BOT_TOKEN")),
                   db=DatabaseConfig(db_name=os.getenv("DB_NAME"), db_host=os.getenv("DB_HOST"), db_user=os.getenv("DB_USER"), db_password=os.getenv("DB_PASSWORD"), db_port=os.getenv("DB_PORT")),
                   redis=RedisConfig(host=os.getenv("REDIS_HOST")),
                   connections=Connections(email_endpoint=os.getenv("GET_ID_BY_EMAIL_HOST"), random_dream_endpoint=os.getenv("GET_RANDOM_DREAM_HOST"), post_dream_endpoint=os.getenv("POST_MY_DREAM_HOST")),
                   mail=Mail(email_user=os.getenv("EMAIL_USER"), email_password=os.getenv("EMAIL_PASSWORD"), email_server=os.getenv("EMAIL_SERVER")))