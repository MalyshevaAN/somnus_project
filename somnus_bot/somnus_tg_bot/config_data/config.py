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
class Config:
    tg_bot: TgBot
    # db: DatabaseConfig
    redis: RedisConfig




def load_config(path:str | None) -> Config:
    return Config(tg_bot=TgBot(token=os.getenv("BOT_TOKEN")), redis=RedisConfig(host=os.getenv("REDIS_HOST")))