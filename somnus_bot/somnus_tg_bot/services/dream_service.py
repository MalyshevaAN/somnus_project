from database.dream_db import dream
import random


def get_random_dream() -> str:
    return dream[random.randint(0, len(dream)-1)]


def add_dream(text: str) -> None:
    dream.append(text)