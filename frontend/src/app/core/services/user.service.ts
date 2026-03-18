import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../interfaces/user';
import { environment } from '../../../environments/environment';

const API = environment.apiUrl + '/management/users';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  constructor(private http: HttpClient) { }

  findAll(): Observable<User[]> {
    return this.http.get<User[]>(`${API}/all`);
  }

  findAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${API}/all_users`);
  }

  findAllTrainers(): Observable<User[]> {
    return this.http.get<User[]>(`${API}/all_trainers`);
  }

  findById(id: number): Observable<User> {
    return this.http.get<User>(`${API}/${id}`);
  }

  create(pojo: User): Observable<User> {
    return this.http.post<User>(API, pojo, this.httpOptions);
  }

  update(id: number, pojo: User): Observable<User> {
    return this.http.put<User>(`${API}/${id}`, pojo, this.httpOptions);
  }

  updatePassword(id: number, pojo: User): Observable<User> {
    return this.http.put<User>(`${API}/pass/${id}`, pojo, this.httpOptions);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${API}/${id}`);
  }
}
