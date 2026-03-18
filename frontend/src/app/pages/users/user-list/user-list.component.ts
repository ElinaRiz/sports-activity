import { Component } from '@angular/core';
import { BehaviorSubject, combineLatest, Observable } from 'rxjs';
import { map, startWith, switchMap, shareReplay } from 'rxjs/operators';
import { User } from '../../../core/interfaces/user';
import { UserStats } from '../../../core/interfaces/stats/user-stats';
import { DataStorageService } from '../../../core/services/data-storage.service';
import { UserService } from '../../../core/services/user.service';
import { AnalyticsService } from '../../../core/services/analytics.service';
import { CustomToastrService } from '../../../core/services/toastr.service';
import { MatDialog } from '@angular/material/dialog';
import { EditUserComponent } from '../edit-user/edit-user.component';

@Component({
  selector: 'app-user-list',
  standalone: false,
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css',
})
export class UserListComponent {
  role = '';

  private search$ = new BehaviorSubject<string>('');
  private searchStats$ = new BehaviorSubject<string>('');
  private refresh$ = new BehaviorSubject<void>(undefined);

  users$: Observable<User[]> = this.refresh$.pipe(
    switchMap(() => this.userService.findAll()),
    startWith([]),
    shareReplay(1)
  );

  filteredUsers$: Observable<User[]> = combineLatest([this.users$, this.search$])
    .pipe(map(([users, search]) =>
      users.filter(u =>
        u.fullName.toUpperCase().includes(search.toUpperCase())
      )
    ));

  stats$: Observable<UserStats[]> = this.refresh$.pipe(
    switchMap(() => this.analyticsService.getAllUsers()),
    startWith([]),
    shareReplay(1)
  );

  filteredStats$: Observable<UserStats[]> = combineLatest([this.stats$, this.searchStats$])
    .pipe(map(([stats, search]) =>
      stats.filter(s =>
        s.userLogin.toUpperCase().includes(search.toUpperCase())
      )
    ));

  constructor(
    private dataStorage: DataStorageService,
    private userService: UserService,
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

  openDialog(title: string, user?: User) {
    const dialogRef = this.dialog.open(EditUserComponent, {
      data: {
        name: title,
        user: user
          ? { ...user }
          : {
            id: 0,
            login: '',
            password: '',
            fullName: '',
            email: '',
            phone: '',
            birthDate: '',
            role: 'USER'
          }
      }
    });

    return dialogRef.afterClosed();
  }

  create() {
    this.openDialog('Добавление пользователя').subscribe(result => {
      if (!result) return;

      this.userService.create(result).subscribe(() => {
        this.toast.success('Успешно', 'Пользователь добавлен!');
        this.refresh$.next();
      });
    });
  }

  update(user: User) {
    this.openDialog('Редактирование пользователя', user).subscribe(result => {
      if (!result) return;

      this.userService.update(result.id, result).subscribe(() => {
        this.toast.success('Успешно', 'Пользователь изменён!');
        this.refresh$.next();
      });
    });
  }

  changePassword(user: User) {
    this.openDialog('Изменение пароля', user).subscribe(result => {
      if (!result) return;

      this.userService.updatePassword(user.id, result.password).subscribe(() => {
        this.toast.success('Успешно', 'Пароль изменён!');
        this.refresh$.next();
      });
    });
  }

  delete(user: User) {
    if (!confirm(`Удалить пользователя: ${user.fullName}?`)) return;

    this.userService.delete(user.id).subscribe(() => {
      this.toast.success('Успешно', 'Пользователь удалён!');
      this.refresh$.next();
    });
  }
}