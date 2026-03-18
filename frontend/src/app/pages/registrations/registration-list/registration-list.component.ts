import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, combineLatest, Observable } from 'rxjs';
import { map, startWith, switchMap, shareReplay } from 'rxjs/operators';
import { Activity } from '../../../core/interfaces/activity';
import { Registration } from '../../../core/interfaces/registration';
import { User } from '../../../core/interfaces/user';
import { DataStorageService } from '../../../core/services/data-storage.service';
import { ActivityService } from '../../../core/services/activity.service';
import { RegistrationService } from '../../../core/services/registration.service';
import { UserService } from '../../../core/services/user.service';
import { CustomToastrService } from '../../../core/services/toastr.service';
import { MatDialog } from '@angular/material/dialog';
import { EditRegistrationComponent } from '../edit-registration/edit-registration.component';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-registration-list',
  standalone: false,
  templateUrl: './registration-list.component.html',
  styleUrl: './registration-list.component.css',
})
export class RegistrationListComponent implements OnInit {
  role = '';
  userId = 0;
  activityId = 0;

  user?: User;
  activity!: Activity;
  userRegistration?: Registration;

  private search$ = new BehaviorSubject<string>('');
  private refresh$ = new BehaviorSubject<void>(undefined);

  registrations$: Observable<Registration[]> = this.refresh$.pipe(
    switchMap(() => this.registrationService.findByActivityId(this.activityId).pipe(
      map(reg => {
        if (this.role === 'USER') {
          this.userRegistration = reg.find(r => r.userId === this.userId);
        }
        return reg;
      })
    )),
    startWith([]),
    shareReplay(1)
  );

  filteredRegistrations$: Observable<Registration[]> = combineLatest([this.registrations$, this.search$])
    .pipe(map(([regs, search]) =>
      regs.filter(r =>
        r.userName.toUpperCase().includes(search.toUpperCase())
      )
    ));

  constructor(
    private dataStorage: DataStorageService,
    private activityService: ActivityService,
    private registrationService: RegistrationService,
    private userService: UserService,
    private toast: CustomToastrService,
    public dialog: MatDialog,
    private route: ActivatedRoute
  ) {
    this.role = this.dataStorage.getRole();
  }

  ngOnInit() {
    if (this.role === 'USER') {
      this.userId = this.dataStorage.getUserId();
      this.userService.findById(this.userId).subscribe(u => this.user = u);
    }

    this.activityId = Number(this.route.snapshot.paramMap.get('activityId'));
    if (this.activityId) {
      this.activityService.findById(this.activityId).subscribe(a => this.activity = a);
    }
  }

  onSearch(value: string) {
    this.search$.next(value);
  }

  openDialog(title: string, registration?: Registration) {
    const dialogRef = this.dialog.open(EditRegistrationComponent, {
      data: {
        name: title,
        registration: registration
          ? { ...registration }
          : {
            id: 0,
            activityId: this.activity.id,
            activityName: this.activity.name,
            userId: this.user?.id ?? 0,
            userName: this.user?.fullName ?? '',
            registrationDate: new Date().toISOString().slice(0, 16),
            paymentStatus: '',
            attendanceStatus: null
          },
        userId: this.userId
      }
    });

    return dialogRef.afterClosed();
  }

  create() {
    this.openDialog('Добавление регистрации').subscribe(result => {
      if (!result) return;

      this.registrationService.create(result).subscribe(() => {
        this.toast.success('Успешно', 'Регистрация добавлена!');
        this.refresh$.next();
      });
    });
  }

  update(registration: Registration) {
    this.openDialog('Редактирование регистрации', registration).subscribe(result => {
      if (!result) return;

      this.registrationService.update(result.id, result).subscribe(() => {
        this.toast.success('Успешно', 'Регистрация изменена!');
        this.refresh$.next();
      });
    });
  }

  delete(registration: Registration) {
    if (!confirm(
      `Удалить регистрацию пользователя "${registration.userName}" на "${registration.activityName}"?`
    )) return;

    this.registrationService.delete(registration.id).subscribe(() => {
      this.toast.success('Успешно', 'Регистрация удалена!');
      this.refresh$.next();
    });
  }
}