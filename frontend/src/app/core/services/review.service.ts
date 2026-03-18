import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Review } from '../interfaces/review';
import { environment } from '../../../environments/environment';

const API = environment.apiUrl + '/management/reviews';

@Injectable({
  providedIn: 'root',
})
export class ReviewService {
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  constructor(private http: HttpClient) { }

  findAll(): Observable<Review[]> {
    return this.http.get<Review[]>(`${API}/all`);
  }

  findById(id: number): Observable<Review> {
    return this.http.get<Review>(`${API}/${id}`);
  }

  findByActivityId(activityId: number): Observable<Review[]> {
      return this.http.get<Review[]>(`${API}/by_activity/${activityId}`);
  }

  create(pojo: Review): Observable<Review> {
    return this.http.post<Review>(API, pojo, this.httpOptions);
  }

  update(id: number, pojo: Review): Observable<Review> {
    return this.http.put<Review>(`${API}/${id}`, pojo, this.httpOptions);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${API}/${id}`);
  }
}
