from aiogram import Router
from aiogram.types import Message
from lexicon.lexicon_ru import LEXICON

router: Router = Router()

@router.message()
async def send_mistake_message(message: Message):
    await message.answer(LEXICON['mistake'])