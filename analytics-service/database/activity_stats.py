from sqlalchemy import Column, Integer, Numeric, TIMESTAMP, VARCHAR
from .db import Base

class ActivityStats(Base):
    __tablename__ = "analytics_activity_stats"

    activity_id = Column(Integer, primary_key=True)
    activity_name = Column(VARCHAR(100), nullable=False, default="none")
    registrations_count = Column(Integer, nullable=False, default=0)
    reviews_count = Column(Integer, nullable=False, default=0)
    avg_rating = Column(Numeric(3,2), nullable=False, default=0)
    last_update = Column(TIMESTAMP, nullable=False)

    def to_dict(self):
        return {
            "activityId": self.activity_id,
            "activityName": self.activity_name,
            "registrationsCount": self.registrations_count,
            "reviewsCount": self.reviews_count,
            "avgRating": float(self.avg_rating),
            "lastUpdate": self.last_update
        }