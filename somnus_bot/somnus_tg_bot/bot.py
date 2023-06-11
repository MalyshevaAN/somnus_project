import asyncio
import logging
from handlers.schedule_handlers import scheduler
from aiogram import Bot, Dispatcher
from config_data.config import load_config, Config
from handlers import other_handlers, user_handlers, schedule_handlers
from aiogram.fsm.storage.memory import MemoryStorage
from keyboards.keyboard_commands import set_first_menu


logger = logging.getLogger(__name__)

async def main():
    logging.basicConfig(
        level=logging.INFO,
        format='%(filename)s:%(lineno)d #%(levelname)-8s '
               '[%(asctime)s] - %(name)s - %(message)s')

    logger.info("Bot is started")

    config:Config = load_config("/home/nastia/javaProjects/SomnusMicro/somnus_bot/somnus_tg_bot/.env")

    storage: MemoryStorage = MemoryStorage()
    bot:Bot = Bot(config.tg_bot.token, parse_mode='HTML')
    dp: Dispatcher = Dispatcher(storage=storage)

    asyncio.create_task(scheduler())
    await set_first_menu(bot=bot)

    dp.include_router(user_handlers.router)
    dp.include_router(other_handlers.router)
    dp.include_router(schedule_handlers.router)


    await bot.delete_webhook(drop_pending_updates=True)
    await dp.start_polling(bot)

if __name__== "__main__":
    asyncio.run(main())

