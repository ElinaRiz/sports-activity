from sqlalchemy import Column, Integer, Numeric, TIMESTAMP, VARCHAR
from .db import Base

class ActivityTypeStats(Base):
    __tablename__ = "analytics_activity_type_stats"

    activity_type_id = Column(Integer, primary_key=True)
    activity_type_name = Column(VARCHAR(100), nullable=False, default="none")
    activities_count = Column(Integer, nullable=False, default=0)
    avg_duration = Column(Numeric(5,2), nullable=False, default=0)
    avg_cost = Column(Numeric(7,2), nullable=False, default=0)
    avg_max_participants = Column(Numeric(5,2), nullable=False, default=0)
    registrations_count = Column(Integer, nullable=False, default=0)
    last_update = Column(TIMESTAMP, nullable=False)

    def to_dict(self):
        return {
            "activityTypeId": self.activity_type_id,
            "activityTypeName": self.activity_type_name,
            "activitiesCount": self.activities_count,
            "avgDuration": float(self.avg_duration),
            "avgCost": float(self.avg_cost),
            "avgMaxParticipants": float(self.avg_max_participants),
            "registrationsCount": self.registrations_count,
            "lastUpdate": self.last_update
        }