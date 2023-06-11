from aiogram import Router
from aiogram.types import Message
from lexicon.lexicon_ru import LEXICON

router: Router = Router()

@router.message()
async def check(message: Message):
    await message.answer(text="Не понимаю тебя :(\nВоспользуйся командой /help, чтобы посмотреть, какие возможности есть у бота")
