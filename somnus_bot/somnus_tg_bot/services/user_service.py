import requests
import json
import dotenv
import os
from requests.exceptions import ConnectionError
import http

dotenv.load_dotenv('/home/nastia/javaProjects/SomnusMicro/somnus_bot/somnus_tg_bot/.env')

URL = os.getenv('GET_ID_BY_EMAIL_HOST')

def get_id_by_email(email:str) -> int:
    data = {'userEmail':email}
    headers = {'content-type':'application/json'}
    try:
        response = requests.post(URL, data=json.dumps(data), headers=headers)
        if (response.status_code == http.HTTPStatus.OK):
            return int(response.json()['id'])

        elif (response.status_code == http.HTTPStatus.NOT_FOUND):
            return -1
    except ConnectionError as e:
        return -2