from fastapi import FastAPI, HTTPException, status, Depends, Response
import asyncio
from core.eureka_client import register_with_eureka, stop_eureka
from event_consumers.rabbit_consumer import consume, close_connection
from event_consumers.stats_updater import update_activity_type_stats, update_trainer_stats
from event_consumers.activity_created_event import ActivityCreatedEvent
from database.db import get_db, engine, Base
from database.activity_type_stats import ActivityTypeStats
from database.activity_stats import ActivityStats
from database.trainer_stats import TrainerStats
from database.user_stats import UserStats
from core.security import role_required
from core.info_printer import print_error
from config import APP_NAME

app = FastAPI(title=APP_NAME)

@app.on_event("startup")
async def startup_event():
    try:
        asyncio.create_task(register_with_eureka())
    except Exception as e:
        print_error("STARTUP", f"Не удалось запустить Eureka: {e}")
    
    try:
        Base.metadata.create_all(bind=engine)
    except Exception as e:
        print_error("STARTUP", f"Не удалось создать метаданные БД: {e}")

    try:
        asyncio.create_task(consume())
    except Exception as e:
        print_error("STARTUP", f"Не удалось запустить RabbitMQ: {e}")

@app.on_event("shutdown")
async def shutdown_event():
    try:
        await close_connection()
        await stop_eureka()
    except Exception as e:
        print_error("SHUTDOWN", f"Не удалось закрыть подключение к RabbitMQ: {e}")


@app.get("/stats/activity_types/all")
def get_all_activity_types_metrics(db=Depends(get_db), login_role=Depends(role_required("ADMIN", "TRAINER"))):
    try:
        rows = db.query(ActivityTypeStats).all()
        return [r.to_dict() for r in rows]
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Статистика по видам занятия не найдена")

@app.get("/stats/activity_types/{type_id}")
def get_activity_type_metrics(type_id: int, db=Depends(get_db), login_role=Depends(role_required("ADMIN", "TRAINER"))):
    try:
        stats = db.get(ActivityTypeStats, type_id)
        if not stats:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail=f"Статистика по виду занятия {type_id} не найдена")
        return stats.to_dict()
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail=f"Статистика по виду занятия {type_id} не найдена")


@app.get("/stats/activities/all")
def get_all_activities_metrics(db=Depends(get_db), login_role=Depends(role_required("ADMIN", "TRAINER", "USER"))):
    try:
        rows = db.query(ActivityStats).all()
        return [r.to_dict() for r in rows]
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Статистика по занятиям не найдена")

@app.get("/stats/activities/{activity_id}")
def get_activity_metrics(activity_id: int, db=Depends(get_db), login_role=Depends(role_required("ADMIN", "TRAINER"))):
    try:
        stats = db.get(ActivityStats, activity_id)
        if not stats:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail=f"Статистика по занятию {activity_id} не найдена")
        return stats.to_dict()
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail=f"Статистика по занятию {activity_id} не найдена")


@app.get("/stats/trainers/all")
def get_all_trainers_metrics(db=Depends(get_db), login_role=Depends(role_required("ADMIN", "USER"))):
    try:
        rows = db.query(TrainerStats).all()
        return [r.to_dict() for r in rows]
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Статистика по тренерам не найдена")
    
@app.get("/stats/trainers/{trainer_id}")
def get_trainer_metrics(trainer_id: int, db=Depends(get_db), login_role=Depends(role_required("ADMIN", "USER", "TRAINER"))):
    try:
        stats = db.get(TrainerStats, trainer_id)
        if not stats:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail=f"Статистика по тренеру {trainer_id} не найдена")
        return stats.to_dict()
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail=f"Статистика по тренеру {trainer_id} не найдена")


@app.get("/stats/users/all")
def get_all_users_metrics(db=Depends(get_db), login_role=Depends(role_required("ADMIN", "TRAINER"))):
    try:
        rows = db.query(UserStats).all()
        return [r.to_dict() for r in rows]
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Статистика по пользователям не найдена")
    
@app.get("/stats/users/{user_id}")
def get_user_metrics(user_id: int, db=Depends(get_db), login_role=Depends(role_required("ADMIN", "USER","TRAINER"))):
    try:
        stats = db.get(UserStats, user_id)
        if not stats:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail=f"Статистика по пользователю {user_id} не найдена")

        return stats.to_dict()
    except Exception as e:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail=f"Статистика по пользователю {user_id} не найдена")


@app.post("/event/activity_created")
def activity_created_event(event: ActivityCreatedEvent, db=Depends(get_db)):
    try:
        update_activity_type_stats(db, event.activityTypeId, event.activityTypeName, event.duration, event.cost, event.maxParticipants)
        update_trainer_stats(db, event.trainerId, event.trainerFullName, activity_id=event.activityId, rating=None)
        db.commit()
        return Response(status_code=204)

    except Exception as e:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Ошибка обработки события")