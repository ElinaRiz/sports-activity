import { Component } from '@angular/core';
import { BehaviorSubject, combineLatest, Observable } from 'rxjs';
import { map, startWith, switchMap, shareReplay } from 'rxjs/operators';
import { Trainer } from '../../../core/interfaces/trainer';
import { TrainerStats } from '../../../core/interfaces/stats/trainer-stats';
import { DataStorageService } from '../../../core/services/data-storage.service';
import { TrainerService } from '../../../core/services/trainer.service';
import { AnalyticsService } from '../../../core/services/analytics.service';
import { CustomToastrService } from '../../../core/services/toastr.service';
import { MatDialog } from '@angular/material/dialog';
import { EditTrainerComponent } from '../edit-trainer/edit-trainer.component';

@Component({
  selector: 'app-trainer-list',
  standalone: false,
  templateUrl: './trainer-list.component.html',
  styleUrl: './trainer-list.component.css',
})
export class TrainerListComponent {
  role = '';

  private search$ = new BehaviorSubject<string>('');
  private searchStats$ = new BehaviorSubject<string>('');
  private refresh$ = new BehaviorSubject<void>(undefined);

  trainers$: Observable<Trainer[]> = this.refresh$.pipe(
    switchMap(() => this.trainerService.findAll()),
    startWith([]),
    shareReplay(1)
  );

  filteredTrainers$: Observable<Trainer[]> = combineLatest([this.trainers$, this.search$])
    .pipe(map(([trainers, search]) =>
      trainers.filter(t =>
        t.userName.toUpperCase().includes(search.toUpperCase())
      )
    ));

  stats$: Observable<TrainerStats[]> = this.refresh$.pipe(
    switchMap(() => this.analyticsService.getAllTrainers()),
    startWith([]),
    shareReplay(1)
  );

  filteredStats$: Observable<TrainerStats[]> = combineLatest([this.stats$, this.searchStats$])
    .pipe(map(([stats, search]) =>
      stats.filter(s =>
        s.trainerFullName.toUpperCase().includes(search.toUpperCase())
      )
    ));

  constructor(
    private dataStorage: DataStorageService,
    private trainerService: TrainerService,
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

  openDialog(title: string, trainer?: Trainer) {
    const dialogRef = this.dialog.open(EditTrainerComponent, {
      data: {
        name: title,
        trainer: trainer
          ? { ...trainer }
          : {
            id: 0,
            userId: 0,
            userName: '',
            specialization: '',
            experience: 0,
            achievements: '',
            status: ''
          }
      }
    });

    return dialogRef.afterClosed();
  }

  create() {
    this.openDialog('Добавление тренера').subscribe(result => {
      if (!result) return;

      this.trainerService.create(result).subscribe(() => {
        this.toast.success('Успешно', 'Тренер добавлен!');
        this.refresh$.next();
      });
    });
  }

  update(trainer: Trainer) {
    this.openDialog('Редактирование тренера', trainer).subscribe(result => {
      if (!result) return;

      this.trainerService.update(result.id, result).subscribe(() => {
        this.toast.success('Успешно', 'Данные тренера изменены!');
        this.refresh$.next();
      });
    });
  }

  delete(trainer: Trainer) {
    if (!confirm(`Удалить тренера: ${trainer.userName}?`)) return;

    this.trainerService.delete(trainer.id).subscribe(() => {
      this.toast.success('Успешно', 'Тренер удалён!');
      this.refresh$.next();
    });
  }
}