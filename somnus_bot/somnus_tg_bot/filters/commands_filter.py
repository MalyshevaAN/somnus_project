from aiogram.filters import BaseFilter
from aiogram.types import Message

class CommandFilter(BaseFilter):
    async def __call__(self, message: Message):
        return message.text.startswith("/")