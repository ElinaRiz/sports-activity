import json
from aio_pika import connect_robust, IncomingMessage
from database.db import SessionLocal
from config import RABBITMQ_URL, RABBIT_QUEUE
from .stats_updater import update_activity_type_stats, update_activity_stats, update_trainer_stats, update_user_stats
from core.info_printer import print_info, print_warning, print_error

rabbit_connection = None
rabbit_channel = None

async def handle_message(message: IncomingMessage):
    async with message.process():
        try:
            payload = json.loads(message.body.decode())
        except Exception as e:
            print_error("RabbitMQ", f"Не удалось прочитать сообщение: {e}")
            return

        event = payload.get("event_type")
        data = payload.get("data")

        try:
            with SessionLocal() as db:
                print(event)
                if event == "registration_created":
                    activity_type_id = data["activityTypeId"]
                    activity_type_name = data["activityTypeName"]
                    activity_id = data["activityId"]
                    activity_name = data["activityName"]
                    trainer_id = data["trainerId"]
                    trainer_full_name = data["trainerFullName"]
                    user_id = data["userId"]
                    user_login = data["userLogin"]

                    update_activity_type_stats(db, activity_type_id, activity_type_name, None, None, None)
                    update_activity_stats(db, activity_id, activity_name, None)
                    update_trainer_stats(db, trainer_id, trainer_full_name, None, None)
                    update_user_stats(db, user_id, user_login, None)

                elif event == "review_created":
                    activity_id = data["activityId"]
                    activity_name = data["activityName"]
                    rating = int(data["rating"])
                    trainer_id = data["trainerId"]
                    trainer_full_name = data["trainerFullName"]
                    user_id = data["userId"]
                    user_login = data["userLogin"]

                    update_activity_stats(db, activity_id, activity_name, rating=rating)
                    update_trainer_stats(db, trainer_id, trainer_full_name, activity_id=None, rating=rating)
                    update_user_stats(db, user_id, user_login, rating=rating)

                else:
                    print_warning("RabbitMQ", f"Неизвестное событие: {event}")

                db.commit()
                print("commit", event)
        except Exception as e:
            print_error("RabbitMQ", f"Не удалось обработать сообщение: {e}")


async def consume():
    global rabbit_connection, rabbit_channel
    try:
        rabbit_connection = await connect_robust(RABBITMQ_URL)
        rabbit_channel = await rabbit_connection.channel()
        queue = await rabbit_channel.declare_queue(RABBIT_QUEUE, durable=True)
        await queue.consume(handle_message)
        print_info("RabbitMQ", "RabbitMQ consumer запущен")
    except Exception as e:
        print_error("RabbitMQ", f"Не удалось запустить RabbitMQ consumer: {e}")

async def close_connection():
    global rabbit_connection
    if rabbit_connection:
        await rabbit_connection.close()
        print_info("RabbitMQ", "Подключение к RabbitMQ закрыто")