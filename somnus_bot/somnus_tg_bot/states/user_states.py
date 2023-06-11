from aiogram.filters.state import State, StatesGroup

class FSMAddDream(StatesGroup):
    add_dream = State()
    add_one_more = State()


class FSMConnectAccounts(StatesGroup):
    send_email = State()
    check_code = State()
    connected = State()