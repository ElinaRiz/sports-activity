import { Component, OnInit, Inject } from '@angular/core';
import { Registration } from '../../../core/interfaces/registration';
import { User } from '../../../core/interfaces/user';
import { UserService } from '../../../core/services/user.service';
import { CustomToastrService } from '../../../core/services/toastr.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-edit-registration',
  standalone: false,
  templateUrl: './edit-registration.component.html',
  styleUrl: './edit-registration.component.css',
})
export class EditRegistrationComponent implements OnInit {
  title = '';
  registrationForm!: FormGroup;
  showId = false;
  onlyView = false;

  users: User[] = [];
  paymentStatuses = ['Ожидает', 'Оплачено', 'Отменено'];

  constructor(
    private userService: UserService,
    private toast: CustomToastrService,
    private formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA)
    public data: { name: string; registration: Registration; userId: number },
    public dialogRef: MatDialogRef<EditRegistrationComponent>
  ) { }

  ngOnInit() {
    const userId = this.data.userId;
    if (userId === 0) {
      this.userService.findAllUsers().subscribe(u => {
        this.users = u;
        if (!u.length) {
          this.toast.error('Ошибка', 'Необходимо добавить минимум одного клиента!');
          this.dialogRef.close();
        }
      });
    } else {
      this.userService.findById(userId).subscribe(u => {
        this.users = [u];
      })
    }

    const r = this.data.registration;
    this.registrationForm = this.formBuilder.group({
      id: [r.id],
      activityId: [r.activityId],
      activityName: [r.activityName],
      userId: [r.userId, [Validators.required]],
      userName: [r.userName],
      registrationDate: [r.registrationDate, [Validators.required]],
      paymentStatus: [r.paymentStatus, [Validators.required]],
      attendanceStatus: [r.attendanceStatus, [Validators.required]]
    });

    this.title = this.data.name;
    switch (this.title) {
      case 'Добавление регистрации':
        this.registrationForm.controls['id'].setValue(0);
        break;
      case 'Редактирование регистрации':
        this.showId = true;
        break;
      case 'Регистрация':
        this.showId = true;
        this.onlyView = true;
        this.registrationForm.disable();
        break;
    }
  }

  submit() {
    if (this.registrationForm.invalid) return;
    this.dialogRef.close(this.registrationForm.value);
  }

  cancel() {
    this.dialogRef.close();
  }
}
