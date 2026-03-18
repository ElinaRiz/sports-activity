import { Injectable } from '@angular/core';

const TOKEN_KEY = 'auth-token';
const ROLE_KEY = 'auth-role';
const USER_ID_KEY = 'auth-user_id';
const LOGIN_KEY = 'auth-user_login';

@Injectable({
  providedIn: 'root',
})
export class DataStorageService {
  constructor() { }

  signOut(): void {
    localStorage.clear();
  }

  public saveData(login: string, userId: number, token: string, role: string): void {
    localStorage.clear();
    localStorage.setItem(LOGIN_KEY, login.toString());
    localStorage.setItem(USER_ID_KEY, userId.toString());
    localStorage.setItem(TOKEN_KEY, token);
    localStorage.setItem(ROLE_KEY, role);
  }

  public getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  public getRole(): string {
    return localStorage.getItem(ROLE_KEY) ?? '';
  }
  

  public getUserId(): number {
    return Number(localStorage.getItem(USER_ID_KEY));
  }

  public getLogin(): string {
    return localStorage.getItem(LOGIN_KEY) ?? '';
  }

  public setLogin(login: string): void {
    localStorage.setItem(LOGIN_KEY, login.toString());
  }
}
