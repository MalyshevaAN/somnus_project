import smtplib

from config_data.config import Config, load_config

config: Config = load_config('somnus_tg_bot/.env')

user = config.mail.email_user
password = config.mail.email_password
server = config.mail.email_server
port = 587

def send_code(recieiver_email:str, code: str) -> bool:
    subject = "Somnus: code for account connection"
    to =  recieiver_email
    charset = 'Content-Type: text/plain; charset=utf-8'
    mime = 'MIME-Version:1.0'
    text = f'Your code: {code}. Do not tell it anyone'

    body = "\r\n".join((f"From:{user}", f"To:{to}",
                    f"Subject:{subject}", mime, charset, "", text))

    try:
        smtp = smtplib.SMTP(server, port)
        smtp.starttls()
        smtp.ehlo()
        smtp.login(user, password)
        smtp.sendmail(user, to, body.encode('utf-8'))
        return True
    except smtplib.SMTPException as err:
        print('something went wrong', err)
        return False
    finally:
        smtp.quit()