import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginRequest } from '../interfaces/login-request';
import { LoginResponse } from '../interfaces/login-response';
import { User } from '../interfaces/user';
import { environment } from '../../../environments/environment';

const API = environment.apiUrl + '/auth/security';
export type LocalDate = string;

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  constructor(private http: HttpClient) { }

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${API}/login`, request, this.httpOptions);
  }

  register(user: Partial<User>): Observable<any> {
    return this.http.post(`${API}/register`, user);
  }
}
