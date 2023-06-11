# keyboardButton, ReplyKeyboardMarkup, ReplyKeyboardRemove

#resize_keyboard

#KeyboardBuilder - row, as_markup()
# keyboard: list[list[KeyboardButton]] = [
#     [KeyboardButton(text=str(i)) for i in range(1, 4)],
#     [KeyboardButton(text=str(i)) for i in range(4, 7)]]


#инлайн кнопки
# InlineKeyboardMarkup, InlineKeyboardButton
#text, url, callback_data - CallbackQuery
# from aiogram.filters import Text
# from aiogram.types import CallbackQuery

# # ...

# # Этот хэндлер будет срабатывать на апдейт типа CallbackQuery
# # с data 'big_button_1_pressed' или 'big_button_2_pressed'
# @dp.callback_query(Text(text=['big_button_1_pressed',
#                               'big_button_2_pressed']))
# async def process_buttons_press(callback: CallbackQuery):
#     await callback.answer()

# # Этот хэндлер будет срабатывать на апдейт типа CallbackQuery
# # с data 'big_button_1_pressed'
# @dp.callback_query(Text(text=['big_button_1_pressed']))
# async def process_button_1_press(callback: CallbackQuery):
#     await callback.message.edit_text(
#         text='Была нажата БОЛЬШАЯ КНОПКА 1',
#         reply_markup=callback.message.reply_markup)


# # Этот хэндлер будет срабатывать на апдейт типа CallbackQuery
# # с data 'big_button_2_pressed'
# @dp.callback_query(Text(text=['big_button_2_pressed']))
# async def process_button_2_press(callback: CallbackQuery):
#     await callback.message.edit_text(
#         text='Была нажата БОЛЬШАЯ КНОПКА 2',
#         reply_markup=callback.message.reply_markup)

#InlineKeyboardBuilder - aiogram.utils.keyboard


# from aiogram.types import InlineKeyboardMarkup, InlineKeyboardButton
# from aiogram.utils.keyboard import InlineKeyboardBuilder
# from <имя_директории_с_проектом>.lexicon import LEXICON


# # Функция для формирования инлайн-клавиатуры на лету
# def create_inline_kb(width: int,
#                      *args: str,
#                      **kwargs: str) -> InlineKeyboardMarkup:
#     # Инициализируем билдер
#     kb_builder: InlineKeyboardBuilder = InlineKeyboardBuilder()
#     # Инициализируем список для кнопок
#     buttons: list[InlineKeyboardButton] = []

#     # Заполняем список кнопками из аргументов args и kwargs
#     if args:
#         for button in args:
#             buttons.append(InlineKeyboardButton(
#                 text=LEXICON[button] if button in LEXICON else button,
#                 callback_data=button))
#     if kwargs:
#         for button, text in kwargs.items():
#             buttons.append(InlineKeyboardButton(
#                 text=text,
#                 callback_data=button))

#     # Распаковываем список с кнопками в билдер методом row c параметром width
#     kb_builder.row(*buttons, width=width)

#     # Возвращаем объект инлайн-клавиатуры
#     return kb_builder.as_markup()

from aiogram.types import InlineKeyboardButton, InlineKeyboardMarkup
from aiogram.utils.keyboard import InlineKeyboardBuilder
from lexicon.lexicon_ru import LEXICON

def create_keyboard(*buttons: str) -> InlineKeyboardMarkup:
    kb_builder: InlineKeyboardBuilder = InlineKeyboardBuilder()
    kb_builder.row(*[InlineKeyboardButton(text=LEXICON[button] if button in LEXICON else button, callback_data=button) for button in buttons], width=1)
    return kb_builder.as_markup()
