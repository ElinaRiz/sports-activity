import os
from dotenv import load_dotenv

load_dotenv()

SERVER_HOST = os.getenv("SERVER_HOST")
SERVER_PORT = int(os.getenv("SERVER_PORT"))
APP_NAME = os.getenv("APP_NAME")
DATABASE_URL = os.getenv("DATABASE_URL")
RABBITMQ_URL = os.getenv("RABBITMQ_URL")
RABBIT_QUEUE = os.getenv("RABBIT_QUEUE")
EUREKA_SERVER = os.getenv("EUREKA_SERVER")
GATEWAY_SECRET = os.getenv("GATEWAY_SECRET")