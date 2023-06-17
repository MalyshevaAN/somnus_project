import requests
import json
from requests.exceptions import ConnectionError
import http
from config_data.config import load_config, Config
from lexicon.lexicon_ru import LEXICON_POSSIBLE_RESPONSE
import logging

config: Config = load_config('somnus_tg_bot/.env')

URL = config.connections.email_endpoint

logger = logging.getLogger(__name__)
logging.basicConfig(
        level=logging.INFO,
        format='%(filename)s:%(lineno)d #%(levelname)-8s '
               '[%(asctime)s] - %(name)s - %(message)s')



def get_user_by_email(email:str):
    data = {'userEmail':email.lower()}
    headers = {'content-type':'application/json'}
    try:
        response = requests.post(URL, data=json.dumps(data), headers=headers)
        if (response.status_code == http.HTTPStatus.OK):
            logger.info("Получена сущность юзера")
            logger.info(response.json())
            print(response.json())
            return response.json()

        elif (response.status_code == http.HTTPStatus.NOT_FOUND):
            logger.info("Пользователь не найден")
            return LEXICON_POSSIBLE_RESPONSE['NOT_FOUND']
    except ConnectionError as e:
        logger.error("Не удается достать данные о пользователе")
        return LEXICON_POSSIBLE_RESPONSE['CONNECTION_ERROR']