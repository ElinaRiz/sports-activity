import { Component } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';
import { User } from '../../../core/interfaces/user';
import { DataStorageService } from '../../../core/services/data-storage.service';
import { UserService } from '../../../core/services/user.service';
import { CustomToastrService } from '../../../core/services/toastr.service';
import { MatDialog } from '@angular/material/dialog';
import { EditUserComponent } from '../../users/edit-user/edit-user.component';

@Component({
  selector: 'app-profile',
  standalone: false,
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent {
  currentUserId!: number;

  private refresh$ = new BehaviorSubject<void>(undefined);
  user$: Observable<User | null> = this.refresh$.pipe(
    switchMap(() => this.userService.findById(this.currentUserId)),
    startWith(null)
  );

  constructor(
    private dataStorage: DataStorageService,
    private userService: UserService,
    private toast: CustomToastrService,
    private dialog: MatDialog
  ) {
    this.currentUserId = this.dataStorage.getUserId();
  }

  openEditUser(title: string, user: User) {
    const dialogRef = this.dialog.open(EditUserComponent, {
      data: {
        name: title,
        user: { ...user }
      }
    });

    return dialogRef.afterClosed();
  }

  updateUser(updatedUser: User) {
    this.openEditUser('Редактирование пользователя', updatedUser).subscribe(result => {
      if (!result) return;

      this.userService.update(this.currentUserId, result).subscribe((u) => {
        this.dataStorage.setLogin(u.login);
        this.toast.success('Успешно', 'Данные профиля обновлены!');
        this.refresh$.next();
      });
    });
  }

  updatePassword(updatedUser: User) {
    this.openEditUser('Изменение пароля', updatedUser).subscribe(result => {
      if (!result) return;

      this.userService.updatePassword(this.currentUserId, result).subscribe(() => {
        this.toast.success('Успешно', 'Пароль обновлён!');
        this.refresh$.next();
      });
    });
  }
}