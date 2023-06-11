from database.email_database import emails

def email_exists(email:str) -> bool:
    return email in emails