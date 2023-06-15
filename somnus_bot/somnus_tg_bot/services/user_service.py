import requests
import json
from requests.exceptions import ConnectionError
import http
from config_data.config import load_config, Config

config: Config = load_config('somnus_tg_bot/.env')

URL = config.connections.email_endpoint

def get_id_by_email(email:str) -> int:
    data = {'userEmail':email.lower()}
    headers = {'content-type':'application/json'}
    try:
        response = requests.post(URL, data=json.dumps(data), headers=headers)
        if (response.status_code == http.HTTPStatus.OK):
            return int(response.json()['id'])

        elif (response.status_code == http.HTTPStatus.NOT_FOUND):
            return -1
    except ConnectionError as e:
        return -2