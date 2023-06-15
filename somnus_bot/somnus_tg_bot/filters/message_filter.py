from aiogram.filters import BaseFilter
from aiogram.types import Message

class IsTextMessage(BaseFilter):
    async def __call__(self, message: Message):
        contains_alpha = False
        for elem in message.text.split(' '):
            if elem.isalpha():
                contains_alpha = True
                break

        return message.text is not None and not message.text.startswith('/') and not message.text.isdigit() and contains_alpha




