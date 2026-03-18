from pydantic import BaseModel

class ActivityCreatedEvent(BaseModel):
    activityId: int
    activityTypeId: int
    activityTypeName: str
    duration: int
    cost: float
    maxParticipants: int
    trainerId: int
    trainerFullName: str