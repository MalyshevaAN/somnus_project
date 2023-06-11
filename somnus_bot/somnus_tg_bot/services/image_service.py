from images.images import GOOD_MORNING_IMAGES, ADD_DREAM_IMAGES
import random

def get_good_morning_image() -> str:
    return GOOD_MORNING_IMAGES[random.randint(0, len(GOOD_MORNING_IMAGES) - 1)]


def get_add_dream_image() -> str:
    return ADD_DREAM_IMAGES[random.randint(0, len(ADD_DREAM_IMAGES) - 1)]