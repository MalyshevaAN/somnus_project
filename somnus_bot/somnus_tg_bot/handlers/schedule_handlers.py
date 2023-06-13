from aiogram import Router, Bot
import dotenv, os
from services.image_service import get_good_morning_image
from services.db_service import get_all_users
import asyncio
from lexicon.lexicon_ru import LEXICON
from keyboards.keyboard_utils import create_keyboard
import aioschedule
import logging

router: Router = Router()

logger = logging.getLogger(__name__)

logging.basicConfig(
        level=logging.INFO,
        format='%(filename)s:%(lineno)d #%(levelname)-8s '
               '[%(asctime)s] - %(name)s - %(message)s')

dotenv.load_dotenv()

bot: Bot = Bot(os.getenv('BOT_TOKEN'))


@router.message()
async def send_good_morning():
    photo = get_good_morning_image()
    all_users = get_all_users()
    if isinstance(all_users, list):
        for user in all_users:
            await bot.send_photo(chat_id=user, photo=photo, caption=LEXICON['good_morning'], reply_markup=create_keyboard('yes','no'))
    else:
        logger.error("cannot connect to somnustg db")


async def scheduler():
    aioschedule.every().day.at('07:00').do(send_good_morning)
    while True:
        await aioschedule.run_pending()
        await asyncio.sleep(1)
