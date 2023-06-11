from aiogram import Bot
from aiogram.types import BotCommand

from lexicon.lexicon_ru import LEXICON_COMMANDS_DESCRIPTION_FIRST, LEXICON_COMMANDS_DESCRIPTION

async def set_first_menu(bot: Bot):
    first_menu_commands = [BotCommand(
        command=command,
        description=description
    ) for command, description in LEXICON_COMMANDS_DESCRIPTION_FIRST.items()]

    await bot.set_my_commands(first_menu_commands)


async def set_main_menu_commands(bot: Bot):
    main_menu_commands = [BotCommand(
        command = command,
        description=description
    ) for command, description in LEXICON_COMMANDS_DESCRIPTION.items()]

    await bot.set_my_commands(main_menu_commands)
