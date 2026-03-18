from datetime import datetime, timezone
from database.activity_type_stats import ActivityTypeStats
from database.activity_stats import ActivityStats
from database.trainer_stats import TrainerStats
from database.user_stats import UserStats

def update_activity_type_stats(db, activity_type_id: int, activity_type_name: str, duration: int | None, cost: float | None, max_participants: int | None):
    now = datetime.now(timezone.utc)
    stats = db.get(ActivityTypeStats, activity_type_id)

    if not stats:
        stats = ActivityTypeStats(
            activity_type_id=activity_type_id,
            activity_type_name=activity_type_name,
            activities_count=0,
            avg_duration=0,
            avg_cost=0,
            avg_max_participants=0,
            registrations_count=0,
            last_update=now)
        db.add(stats)
    
    stats.activity_type_name = activity_type_name
    if ((duration is not None) and (cost is not None) and (max_participants is not None)):
        count = stats.activities_count + 1
        stats.avg_duration = ((float(stats.avg_duration) * stats.activities_count) + duration) / count
        stats.avg_cost = ((float(stats.avg_cost) * stats.activities_count) + cost) / count
        stats.avg_max_participants = ((float(stats.avg_max_participants) * stats.activities_count) + max_participants) / count
        stats.activities_count = count
    else:
        stats.registrations_count += 1
    stats.last_update = now

def update_activity_stats(db, activity_id: int, activity_name: str, rating: int | None):
    now = datetime.now(timezone.utc)
    stats = db.get(ActivityStats, activity_id)

    if not stats:
        stats = ActivityStats(
            activity_id=activity_id,
            activity_name=activity_name,
            registrations_count=0,
            reviews_count=0,
            avg_rating=0,
            last_update=now)
        db.add(stats)
    
    stats.activity_name = activity_name
    if (rating is None):
        stats.registrations_count += 1
    else:  
        count = stats.reviews_count + 1
        stats.avg_rating = ((float(stats.avg_rating) * stats.reviews_count) + rating) / count
        stats.reviews_count = count
    stats.last_update = now

def update_trainer_stats(db, trainer_id: int, trainer_full_name: str, activity_id: int | None, rating: int | None):
    now = datetime.now(timezone.utc)
    stats = db.get(TrainerStats, trainer_id)

    if not stats:
        stats = TrainerStats(
            trainer_id=trainer_id,
            trainer_full_name=trainer_full_name,
            activities_count = 0,
            registrations_count=0,
            reviews_count=0,
            avg_rating=0,
            last_update=now)
        db.add(stats)
    
    stats.trainer_full_name = trainer_full_name
    if (activity_id is not None):
        stats.activities_count += 1
    if (rating is None):
        stats.registrations_count += 1
    else:  
        count = stats.reviews_count + 1
        stats.avg_rating = ((float(stats.avg_rating) * stats.reviews_count) + rating) / count
        stats.reviews_count = count
    stats.last_update = now

def update_user_stats(db, user_id: int, user_login: str, rating: int | None):
    now = datetime.now(timezone.utc)
    stats = db.get(UserStats, user_id)

    if not stats:
        stats = UserStats(
            user_id=user_id,
            user_login=user_login,
            registrations_count=0,
            reviews_count=0,
            avg_given_rating=0,
            last_update=now)
        db.add(stats)
    
    stats.user_login = user_login
    if (rating is None):
        stats.registrations_count += 1
    else:  
        count = stats.reviews_count + 1
        stats.avg_given_rating = ((float(stats.avg_given_rating) * stats.reviews_count) + rating) / count
        stats.reviews_count = count
    stats.last_update = now  