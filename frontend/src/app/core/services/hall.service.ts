import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Hall } from '../interfaces/hall';
import { environment } from '../../../environments/environment';

const API = environment.apiUrl + '/management/halls';

@Injectable({
  providedIn: 'root',
})
export class HallService {
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  constructor(private http: HttpClient) { }

  findAll(): Observable<Hall[]> {
    return this.http.get<Hall[]>(`${API}/all`);
  }

  findById(id: number): Observable<Hall> {
    return this.http.get<Hall>(`${API}/${id}`);
  }

  create(pojo: Hall): Observable<Hall> {
    return this.http.post<Hall>(API, pojo, this.httpOptions);
  }

  update(id: number, pojo: Hall): Observable<Hall> {
    return this.http.put<Hall>(`${API}/${id}`, pojo, this.httpOptions);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${API}/${id}`);
  }
}
