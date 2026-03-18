import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Trainer } from '../interfaces/trainer';
import { environment } from '../../../environments/environment';

const API = environment.apiUrl + '/management/trainers';

@Injectable({
  providedIn: 'root',
})
export class TrainerService {
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  constructor(private http: HttpClient) { }

  findAll(): Observable<Trainer[]> {
    return this.http.get<Trainer[]>(`${API}/all`);
  }

  findById(id: number): Observable<Trainer> {
    return this.http.get<Trainer>(`${API}/${id}`);
  }

  create(pojo: Trainer): Observable<Trainer> {
    return this.http.post<Trainer>(API, pojo, this.httpOptions);
  }

  update(id: number, pojo: Trainer): Observable<Trainer> {
    return this.http.put<Trainer>(`${API}/${id}`, pojo, this.httpOptions);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${API}/${id}`);
  }
}
