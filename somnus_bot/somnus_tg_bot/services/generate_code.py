import random
import logging

def generate_code() -> str:
    code = ''
    for i in range(6):
        code += str(random.randint(1,9))

    return code