export enum ERole {
  USER = "USER",
  ADMIN = "ADMIN",
  TRAINER = "TRAINER"
}

export type LocalDate = string;

export interface User {
  id: number;
  login: string;
  password: string;
  role: ERole;
  fullName: string;
  email: string;
  phone: string;
  birthDate: LocalDate;
}