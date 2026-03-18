import { Component } from '@angular/core';
import { BehaviorSubject, combineLatest, Observable } from 'rxjs';
import { map, startWith, switchMap, shareReplay } from 'rxjs/operators';
import { Activity } from '../../../core/interfaces/activity';
import { ActivityStats } from '../../../core/interfaces/stats/activity-stats';
import { DataStorageService } from '../../../core/services/data-storage.service';
import { ActivityService } from '../../../core/services/activity.service';
import { AnalyticsService } from '../../../core/services/analytics.service';
import { CustomToastrService } from '../../../core/services/toastr.service';
import { MatDialog } from '@angular/material/dialog';
import { EditActivityComponent } from '../edit-activity/edit-activity.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-activity-list',
  standalone: false,
  templateUrl: './activity-list.component.html',
  styleUrl: './activity-list.component.css',
})
export class ActivityListComponent {
  role = '';

  private search$ = new BehaviorSubject<string>('');
  private searchStats$ = new BehaviorSubject<string>('');
  private refresh$ = new BehaviorSubject<void>(undefined);

  activities$: Observable<Activity[]> = this.refresh$.pipe(
    switchMap(() => this.activityService.findAll()),
    startWith([]),
    shareReplay(1)
  );

  filteredActivities$ = combineLatest([this.activities$, this.search$])
    .pipe(map(([activities, search]) =>
      activities.filter(a =>
        a.name.toUpperCase().includes(search.toUpperCase())
      )
    ));

  stats$: Observable<ActivityStats[]> = this.refresh$.pipe(
    switchMap(() => this.analyticsService.getAllActivities()),
    startWith([]),
    shareReplay(1)
  );

  filteredStats$ = combineLatest([this.stats$, this.searchStats$])
    .pipe(map(([stats, search]) =>
      stats.filter(s =>
        s.activityName.toUpperCase().includes(search.toUpperCase())
      )
    ));

  constructor(
    private dataStorage: DataStorageService,
    private activityService: ActivityService,
    private analyticsService: AnalyticsService,
    private toast: CustomToastrService,
    public dialog: MatDialog,
    private router: Router
  ) {
    this.role = this.dataStorage.getRole();
  }

  onSearch(value: string) {
    this.search$.next(value);
  }

  onSearchInStats(value: string) {
    this.searchStats$.next(value);
  }

  openDialog(title: string, activity?: Activity) {
    const dialogRef = this.dialog.open(EditActivityComponent, {
      data: {
        name: title,
        activity: activity
          ? { ...activity }
          : {
            id: 0,
            name: '',
            activityTypeId: 0,
            activityTypeName: '',
            trainerId: 0,
            trainerName: '',
            hallId: 0,
            hallRoom: 0,
            activityLevel: '',
            startTime: '',
            duration: 0,
            maxParticipants: 0,
            cost: 0,
            status: ''
          }
      }
    });

    return dialogRef.afterClosed();
  }

  create() {
    this.openDialog('Добавление занятия').subscribe(result => {
      if (!result) return;

      this.activityService.create(result).subscribe(() => {
        this.toast.success('Успешно', 'Активность добавлена!');
        this.refresh$.next();
      });
    });
  }

  update(activity: Activity) {
    this.openDialog('Редактирование занятия', activity).subscribe(result => {
      if (!result) return;

      this.activityService.update(result.id, result).subscribe(() => {
        this.toast.success('Успешно', 'Активность изменена!');
        this.refresh$.next();
      });
    });
  }

  delete(activity: Activity) {
    if (!confirm(`Удалить активность: ${activity.name}?`)) return;

    this.activityService.delete(activity.id).subscribe(() => {
      this.toast.success('Успешно', 'Активность удалена!');
      this.refresh$.next();
    });
  }

  goToRegistrations(activityId: number) {
    this.router.navigate(['/activities', activityId, 'registrations']);
  }

  goToReviews(activityId: number) {
    this.router.navigate(['/activities', activityId, 'reviews']);
  }
}