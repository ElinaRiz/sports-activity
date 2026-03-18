export interface Review {
  id: number;
  userId: number;
  userName: string;
  registrationId: number;
  registrationActivityName: string;
  rating: number;
  comment: string;
}