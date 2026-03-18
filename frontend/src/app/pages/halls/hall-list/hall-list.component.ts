import { Component } from '@angular/core';
import { BehaviorSubject, combineLatest, Observable } from 'rxjs';
import { map, startWith, switchMap, shareReplay } from 'rxjs/operators';
import { Hall } from '../../../core/interfaces/hall';
import { DataStorageService } from '../../../core/services/data-storage.service';
import { HallService } from '../../../core/services/hall.service';
import { CustomToastrService } from '../../../core/services/toastr.service';
import { MatDialog } from '@angular/material/dialog';
import { EditHallComponent } from '../edit-hall/edit-hall.component';

@Component({
  selector: 'app-hall-list',
  standalone: false,
  templateUrl: './hall-list.component.html',
  styleUrl: './hall-list.component.css',
})
export class HallListComponent {
  role = '';

  private search$ = new BehaviorSubject<string>('');
  private refresh$ = new BehaviorSubject<void>(undefined);

  halls$: Observable<Hall[]> = this.refresh$.pipe(
    switchMap(() => this.hallService.findAll()),
    startWith([]),
    shareReplay(1)
  );

  filteredHalls$: Observable<Hall[]> = combineLatest([this.halls$, this.search$])
    .pipe(map(([halls, search]) =>
      halls.filter(h =>
        h.name.toUpperCase().includes(search.toUpperCase())
      )
    ));

  constructor(
    private dataStorage: DataStorageService,
    private hallService: HallService,
    private toast: CustomToastrService,
    public dialog: MatDialog
  ) {
    this.role = this.dataStorage.getRole();
  }

  onSearch(value: string) {
    this.search$.next(value);
  }

  openDialog(title: string, hall?: Hall) {
    const dialogRef = this.dialog.open(EditHallComponent, {
      data: {
        name: title,
        hall: hall
          ? { ...hall }
          : {
            id: 0,
            name: '',
            room: '',
            capacity: 0
          }
      }
    });

    return dialogRef.afterClosed();
  }

  create() {
    this.openDialog('Добавление зала').subscribe(result => {
      if (!result) return;

      this.hallService.create(result).subscribe(() => {
        this.toast.success('Успешно', 'Зал добавлен!');
        this.refresh$.next();
      });
    });
  }

  update(hall: Hall) {
    this.openDialog('Редактирование зала', hall).subscribe(result => {
      if (!result) return;

      this.hallService.update(result.id, result).subscribe(() => {
        this.toast.success('Успешно', 'Зал изменён!');
        this.refresh$.next();
      });
    });
  }

  delete(hall: Hall) {
    if (!confirm(`Удалить зал: ${hall.name}?`)) return;

    this.hallService.delete(hall.id).subscribe(() => {
      this.toast.success('Успешно', 'Зал удалён!');
      this.refresh$.next();
    });
  }
}