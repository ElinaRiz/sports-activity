import { Component } from '@angular/core';
import { BehaviorSubject, combineLatest, Observable } from 'rxjs';
import { map, startWith, switchMap, shareReplay } from 'rxjs/operators';
import { ActivityType } from '../../../core/interfaces/activity-type';
import { ActivityTypeStats } from '../../../core/interfaces/stats/activity-type-stats';
import { DataStorageService } from '../../../core/services/data-storage.service';
import { ActivityTypeService } from '../../../core/services/activity-type.service';
import { AnalyticsService } from '../../../core/services/analytics.service';
import { CustomToastrService } from '../../../core/services/toastr.service';
import { MatDialog } from '@angular/material/dialog';
import { EditActivityTypeComponent } from '../edit-activity-type/edit-activity-type.component';

@Component({
  selector: 'app-activity-type-list',
  standalone: false,
  templateUrl: './activity-type-list.component.html',
  styleUrl: './activity-type-list.component.css',
})
export class ActivityTypeListComponent {
  role = '';

  private search$ = new BehaviorSubject<string>('');
  private searchStats$ = new BehaviorSubject<string>('');
  private refresh$ = new BehaviorSubject<void>(undefined);

  activityTypes$: Observable<ActivityType[]> = this.refresh$.pipe(
    switchMap(() => this.activityTypeService.findAll()),
    startWith([]),
    shareReplay(1)
  );

  filteredActivityTypes$ = combineLatest([this.activityTypes$, this.search$])
    .pipe(map(([activityTypes, search]) =>
      activityTypes.filter(a =>
        a.name.toUpperCase().includes(search.toUpperCase())
      )
    ));

  stats$: Observable<ActivityTypeStats[]> = this.refresh$.pipe(
    switchMap(() => this.analyticsService.getAllActivityTypes()),
    startWith([]),
    shareReplay(1)
  );

  filteredStats$ = combineLatest([this.stats$, this.searchStats$])
    .pipe(map(([stats, search]) =>
      stats.filter(s =>
        s.activityTypeName.toUpperCase().includes(search.toUpperCase())
      )
    ));

  constructor(
    private dataStorage: DataStorageService,
    private activityTypeService: ActivityTypeService,
    private analyticsService: AnalyticsService,
    private toast: CustomToastrService,
    public dialog: MatDialog
  ) {
    this.role = this.dataStorage.getRole();
  }

  onSearch(value: string) {
    this.search$.next(value);
  }

  onSearchInStats(value: string) {
    this.searchStats$.next(value);
  }

  openDialog(title: string, activityType?: ActivityType) {
    const dialogRef = this.dialog.open(EditActivityTypeComponent, {
      data: {
        name: title,
        activityType: activityType
          ? { ...activityType }
          : {
            id: 0,
            name: '',
            baseDuration: 0,
            baseCost: 0
          }
      }
    });

    return dialogRef.afterClosed();
  }

  create() {
    this.openDialog('Добавление вида занятий').subscribe(result => {
      if (!result) return;

      this.activityTypeService.create(result).subscribe(() => {
        this.toast.success('Успешно', 'Вид активности добавлен!');
        this.refresh$.next();
      });
    });
  }

  update(activityType: ActivityType) {
    this.openDialog('Редактирование вида занятий', activityType).subscribe(result => {
      if (!result) return;

      this.activityTypeService.update(result.id, result).subscribe(() => {
        this.toast.success('Успешно', 'Вид активности изменён!');
        this.refresh$.next();
      });
    });
  }

  delete(activityType: ActivityType) {
    if (!confirm(`Удалить вид активности: ${activityType.name}?`)) return;

    this.activityTypeService.delete(activityType.id).subscribe(() => {
      this.toast.success('Успешно', 'Вид активности удалён!');
      this.refresh$.next();
    });
  }
}