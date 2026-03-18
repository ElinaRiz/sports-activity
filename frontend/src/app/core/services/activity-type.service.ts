import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ActivityType } from '../interfaces/activity-type';
import { environment } from '../../../environments/environment';

const API = environment.apiUrl + '/management/activity_types';

@Injectable({
  providedIn: 'root',
})
export class ActivityTypeService {
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  constructor(private http: HttpClient) { }

  findAll(): Observable<ActivityType[]> {
    return this.http.get<ActivityType[]>(`${API}/all`);
  }

  findById(id: number): Observable<ActivityType> {
    return this.http.get<ActivityType>(`${API}/${id}`);
  }

  create(pojo: ActivityType): Observable<ActivityType> {
    return this.http.post<ActivityType>(API, pojo, this.httpOptions);
  }

  update(id: number, pojo: ActivityType): Observable<ActivityType> {
    return this.http.put<ActivityType>(`${API}/${id}`, pojo, this.httpOptions);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${API}/${id}`);
  }
}
