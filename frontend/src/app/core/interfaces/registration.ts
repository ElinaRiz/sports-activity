export type LocalDateTime = string;

export interface Registration {
  id: number;
  activityId: number;
  activityName: string;
  userId: number;
  userName: string;
  registrationDate: LocalDateTime;
  paymentStatus: string;
  attendanceStatus: boolean | null;
}