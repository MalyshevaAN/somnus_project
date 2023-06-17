import requests, json
import dotenv, os
import http
from requests.exceptions import ConnectionError
from services.db_service import get_user_somnus_id
from lexicon.lexicon_ru import LEXICON_POSSIBLE_RESPONSE
from config_data.config import Config, load_config

config: Config = load_config('somnus_tg_bot/.env')

URL_GET = config.connections.random_dream_endpoint

URL_POST = config.connections.post_dream_endpoint

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
        somnus_id, somnus_author_username = get_user_somnus_id(int(user_id))
        if somnus_id > 0:
            data = {'text':text, 'authorId': somnus_id, 'authorUsername': somnus_author_username}
            headers = {'content-type':'application/json'}
            response = requests.post(URL_POST, data=json.dumps(data), headers=headers)
            if (response.status_code == http.HTTPStatus.OK):
                return LEXICON_POSSIBLE_RESPONSE['OK']
            if (response.status_code == http.HTTPStatus.BAD_REQUEST):
                return LEXICON_POSSIBLE_RESPONSE['BAD_REQUEST']
        else:
            return LEXICON_POSSIBLE_RESPONSE['CONNECTION_ERROR']

    except ConnectionError as e:
        return LEXICON_POSSIBLE_RESPONSE['CONNECTION_ERROR_TG']


