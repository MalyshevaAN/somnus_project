import requests, json
import dotenv, os
import http
from requests.exceptions import ConnectionError
from database.database import user_connected
from lexicon.lexicon_ru import LEXICON_POSSIBLE_RESPONSE

dotenv.load_dotenv('somnus_tg_bot/.env')


URL_GET = os.getenv('GET_RANDOM_DREAM_HOST')

URL_POST = os.getenv('POST_MY_DREAM_HOST')

def get_random_dream() -> str|int:
    try:
        response = requests.get(URL_GET)
        if (response.status_code == http.HTTPStatus.OK):
            return response.json()['dreamText']

        elif (response.status_code == http.HTTPStatus.NOT_FOUND):
            return LEXICON_POSSIBLE_RESPONSE['NOT_FOUND']
    except ConnectionError as e:
        return LEXICON_POSSIBLE_RESPONSE['CONNECTION_ERROR']

def add_my_dream(text: str, user_id:str) -> int:
    try:
        user_somnus_id = user_connected[user_id]
        data = {'text':text, 'authorId':user_somnus_id}
        headers = {'content-type':'application/json'}
        response = requests.post(URL_POST, data=json.dumps(data), headers=headers)
        if (response.status_code == http.HTTPStatus.OK):
            return LEXICON_POSSIBLE_RESPONSE['OK']
        if (response.status_code == http.HTTPStatus.BAD_REQUEST):
            return LEXICON_POSSIBLE_RESPONSE['BAD_REQUEST']

    except ConnectionError as e:
        return LEXICON_POSSIBLE_RESPONSE['CONNECTION_ERROR']


