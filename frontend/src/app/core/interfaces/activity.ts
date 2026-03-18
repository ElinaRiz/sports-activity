export type LocalDateTime = string;

export interface Activity {
  id: number;
  name: string;
  activityTypeId: number;
  activityTypeName: string;
  trainerId: number;
  trainerName: string;
  hallId: number;
  hallRoom: number;
  activityLevel: string;
  startTime: LocalDateTime;
  duration: number;
  maxParticipants: number;
  cost: number;
  status: string;
}