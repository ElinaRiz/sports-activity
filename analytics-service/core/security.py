import hmac
import hashlib
import base64
from fastapi import Header, HTTPException, status, Depends
from config import GATEWAY_SECRET

class CurrentLoginRole:
    def __init__(self, login: str, role: str):
        self.login = login
        self.role = role

def generate_secret(login: str, role: str):
    data = f"{login}:{role}".encode()
    h = hmac.new(GATEWAY_SECRET.encode(), data, hashlib.sha256)
    return base64.b64encode(h.digest()).decode()

async def get_current_user(x_login: str = Header(None), x_role: str = Header(None), x_secret: str = Header(None)) -> CurrentLoginRole:
    if not x_login or not x_role or not x_secret:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Необходимо войти в систему")
        
    g_secret = generate_secret(x_login, x_role)
    if x_secret != g_secret:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Необходимо войти в систему")

    return CurrentLoginRole(x_login, x_role)

def role_required(*allowed_roles):
    def wrapper(user: CurrentLoginRole = Depends(get_current_user)):
        if user.role not in allowed_roles:
            raise HTTPException(status_code=status.HTTP_403_FORBIDDEN)
        return user
    return wrapper