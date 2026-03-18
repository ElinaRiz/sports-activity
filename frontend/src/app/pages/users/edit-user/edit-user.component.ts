import { Component, OnInit, Inject } from '@angular/core';
import { User, ERole } from '../../../core/interfaces/user';
import { DataStorageService } from '../../../core/services/data-storage.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-edit-user',
  standalone: false,
  templateUrl: './edit-user.component.html',
  styleUrl: './edit-user.component.css',
})
export class EditUserComponent implements OnInit {
  title = '';
  userForm!: FormGroup;
  showId = false;
  onlyView = false;
  flagPass = false;

  role = '';
  roles = Object.values(ERole);

  constructor(
    private dataStorage: DataStorageService,
    private formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA)
    public data: { name: string; user: User },
    public dialogRef: MatDialogRef<EditUserComponent>
  ) { }

  ngOnInit() {
    this.role = this.dataStorage.getRole();

    const u = this.data.user;
    this.userForm = this.formBuilder.group({
      id: [u.id],
      login: [u.login, [Validators.required]],
      password: [u.password, [Validators.required]],
      fullName: [u.fullName, [Validators.required]],
      email: [u.email, [Validators.required, Validators.email]],
      phone: [u.phone, [Validators.required]],
      birthDate: [u.birthDate, [Validators.required]],
      role: [u.role, [Validators.required]]
    });

    this.title = this.data.name;
    switch (this.title) {
      case 'Добавление пользователя':
        this.userForm.controls['id'].setValue(0);
        break;
      case 'Редактирование пользователя':
        this.showId = true;
        this.userForm.controls['password'].setValue('password');
        break;
      case 'Изменение пароля':
        this.showId = true;
        this.flagPass = true;
        break;
      case 'Пользователь':
        this.showId = true;
        this.onlyView = true;
        this.userForm.disable();
        break;
    }
  }

  submit() {
    if (this.userForm.invalid) return;
    this.dialogRef.close(this.userForm.value);
  }

  cancel(): void {
    this.dialogRef.close();
  }
}