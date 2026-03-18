from py_eureka_client import eureka_client
from config import EUREKA_SERVER, APP_NAME, SERVER_HOST, SERVER_PORT
from .info_printer import print_info, print_error

async def register_with_eureka():
    try:
        await eureka_client.init_async(eureka_server=EUREKA_SERVER, app_name=APP_NAME, instance_host=SERVER_HOST, instance_port=SERVER_PORT)
        print_info("Eureka", f"{APP_NAME} зарегистрирован на {SERVER_PORT}")
    except Exception as e:
        print_error("Eureka", f"Не удалось зарегистрироваться в Eureka: {e}")

async def stop_eureka():
    await eureka_client.stop()
    print_info("Eureka", "Подключение к Eureka закрыто")