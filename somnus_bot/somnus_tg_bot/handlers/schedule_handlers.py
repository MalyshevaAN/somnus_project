from aiogram import Router, Bot
import dotenv, os
from services.image_service import get_good_morning_image
from services.db_service import get_users
from database.database import user_connected
import asyncio
from lexicon.lexicon_ru import LEXICON
from keyboards.keyboard_utils import create_keyboard
import aioschedule

router: Router = Router()


dotenv.load_dotenv()

bot: Bot = Bot(os.getenv('BOT_TOKEN'))


@router.message()
async def send_good_morning():
    photo = get_good_morning_image()
    for user in user_connected:
        await bot.send_photo(chat_id=user, photo=photo, caption=LEXICON['good_morning'], reply_markup=create_keyboard('yes','no'))


async def scheduler():
    aioschedule.every().day.at('07:00').do(send_good_morning)
    while True:
        await aioschedule.run_pending()
        await asyncio.sleep(1)
