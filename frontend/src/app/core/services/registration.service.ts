import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Registration } from '../interfaces/registration';
import { environment } from '../../../environments/environment';

const API = environment.apiUrl + '/management/registrations';

@Injectable({
  providedIn: 'root',
})
export class RegistrationService {
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  constructor(private http: HttpClient) { }

  findAll(): Observable<Registration[]> {
    return this.http.get<Registration[]>(`${API}/all`);
  }

  findById(id: number): Observable<Registration> {
    return this.http.get<Registration>(`${API}/${id}`);
  }

  findByUserIdAndActivityId(userId: number, activityId: number): Observable<Registration> {
    return this.http.get<Registration>(`${API}/user/${userId}/activity/${activityId}`);
  }

  findByActivityId(activityId: number): Observable<Registration[]> {
    return this.http.get<Registration[]>(`${API}/by_activity/${activityId}`);
  }

  create(pojo: Registration): Observable<Registration> {
    return this.http.post<Registration>(API, pojo, this.httpOptions);
  }

  update(id: number, pojo: Registration): Observable<Registration> {
    return this.http.put<Registration>(`${API}/${id}`, pojo, this.httpOptions);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${API}/${id}`);
  }
}
