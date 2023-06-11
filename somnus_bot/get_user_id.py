import requests
import json

url = 'http://localhost:8081/idByEmail'

data = {"userEmail":"nast"}

headers = {'content-type':'application/json'}

response = requests.post(url, data=json.dumps(data), headers=headers)
if (response.status_code == 200):
    print(response.json()['id'])
else:
    print(response.status_code)