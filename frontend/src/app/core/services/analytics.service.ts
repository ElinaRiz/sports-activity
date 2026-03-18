import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

import { ActivityTypeStats } from '../interfaces/stats/activity-type-stats';
import { ActivityStats } from '../interfaces/stats/activity-stats';
import { TrainerStats } from '../interfaces/stats/trainer-stats';
import { UserStats } from '../interfaces/stats/user-stats';

const API = environment.apiUrl + '/analytics';

@Injectable({
    providedIn: 'root',
})
export class AnalyticsService {
    httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    };

    constructor(private http: HttpClient) { }

    getAllActivityTypes(): Observable<ActivityTypeStats[]> {
        return this.http.get<ActivityTypeStats[]>(`${API}/stats/activity_types/all`);
    }

    getActivityTypeById(typeId: number): Observable<ActivityTypeStats> {
        return this.http.get<ActivityTypeStats>(`${API}/stats/activity_types/${typeId}`);
    }

    getAllActivities(): Observable<ActivityStats[]> {
        return this.http.get<ActivityStats[]>(`${API}/stats/activities/all`);
    }

    getActivityById(activityId: number): Observable<ActivityStats> {
        return this.http.get<ActivityStats>(`${API}/stats/activities/${activityId}`);
    }

    getAllTrainers(): Observable<TrainerStats[]> {
        return this.http.get<TrainerStats[]>(`${API}/stats/trainers/all`);
    }

    getTrainerById(trainerId: number): Observable<TrainerStats> {
        return this.http.get<TrainerStats>(`${API}/stats/trainers/${trainerId}`);
    }

    getAllUsers(): Observable<UserStats[]> {
        return this.http.get<UserStats[]>(`${API}/stats/users/all`);
    }

    getUserById(userId: number): Observable<UserStats> {
        return this.http.get<UserStats>(`${API}/stats/users/${userId}`);
    }
}