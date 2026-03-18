from sqlalchemy import Column, Integer, Numeric, TIMESTAMP, VARCHAR
from .db import Base

class UserStats(Base):
    __tablename__ = "analytics_user_stats"
    user_id = Column(Integer, primary_key=True)
    user_login = Column(VARCHAR(50), nullable=False, default="none")
    registrations_count = Column(Integer, nullable=False, default=0)
    reviews_count = Column(Integer, nullable=False, default=0)
    avg_given_rating = Column(Numeric(3,2), nullable=False, default=0)
    last_update = Column(TIMESTAMP, nullable=False)

    def to_dict(self):
        return {
            "userId": self.user_id,
            "userLogin": self.user_login,
            "registrationsCount": self.registrations_count,
            "reviewsCount": self.reviews_count,
            "avgGivenRating": float(self.avg_given_rating),
            "lastUpdate": self.last_update
        }