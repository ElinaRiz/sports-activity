import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Activity } from '../interfaces/activity';
import { environment } from '../../../environments/environment';

const API = environment.apiUrl + '/management/activities';

@Injectable({
  providedIn: 'root',
})
export class ActivityService {
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  constructor(private http: HttpClient) { }

  findAll(): Observable<Activity[]> {
    return this.http.get<Activity[]>(`${API}/all`);
  }

  findById(id: number): Observable<Activity> {
    return this.http.get<Activity>(`${API}/${id}`);
  }

  create(pojo: Activity): Observable<Activity> {
    return this.http.post<Activity>(API, pojo, this.httpOptions);
  }

  update(id: number, pojo: Activity): Observable<Activity> {
    return this.http.put<Activity>(`${API}/${id}`, pojo, this.httpOptions);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${API}/${id}`);
  }
}