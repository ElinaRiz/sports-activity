from sqlalchemy import Column, Integer, Numeric, TIMESTAMP, VARCHAR
from .db import Base

class TrainerStats(Base):
    __tablename__ = "analytics_trainer_stats"

    trainer_id = Column(Integer, primary_key=True)
    trainer_full_name = Column(VARCHAR(200), nullable=False, default="none")
    activities_count = Column(Integer, nullable=False, default=0)
    registrations_count = Column(Integer, nullable=False, default=0)
    reviews_count = Column(Integer, nullable=False, default=0)
    avg_rating = Column(Numeric(3,2), nullable=False, default=0)
    last_update = Column(TIMESTAMP, nullable=False)

    def to_dict(self):
        return {
            "trainerId": self.trainer_id,
            "trainerFullName": self.trainer_full_name,
            "activitiesCount": self.activities_count,
            "registrationsCount": self.registrations_count,
            "reviewsCount": self.reviews_count,
            "avgRating": float(self.avg_rating),
            "lastUpdate": self.last_update
        }