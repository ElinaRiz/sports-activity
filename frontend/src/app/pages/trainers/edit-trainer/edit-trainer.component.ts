import { Component, OnInit, Inject } from '@angular/core';
import { Trainer } from '../../../core/interfaces/trainer';
import { User } from '../../../core/interfaces/user';
import { UserService } from '../../../core/services/user.service';
import { CustomToastrService } from '../../../core/services/toastr.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-edit-trainer',
  standalone: false,
  templateUrl: './edit-trainer.component.html',
  styleUrl: './edit-trainer.component.css',
})
export class EditTrainerComponent implements OnInit {
  title = '';
  trainerForm!: FormGroup;
  showId = false;
  onlyView = false;

  users: User[] = [];
  statusList = ['Работает', 'В отпуске', 'Уволен']

  constructor(
    private userService: UserService,
    private toast: CustomToastrService,
    private formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA)
    public data: { name: string; trainer: Trainer },
    public dialogRef: MatDialogRef<EditTrainerComponent>
  ) { }

  ngOnInit() {
    this.userService.findAllTrainers().subscribe(u => {
      this.users = u;
      if (!u.length) {
        this.toast.error('Ошибка', 'Необходимо добавить минимум одного пользователя!');
        this.dialogRef.close();
      }
    });

    const t = this.data.trainer;
    this.trainerForm = this.formBuilder.group({
      id: [t.id],
      userId: [t.userId, [Validators.required]],
      specialization: [t.specialization, [Validators.required]],
      experience: [t.experience, [Validators.required, Validators.min(1)]],
      achievements: [t.achievements, [Validators.required]],
      status: [t.status, [Validators.required]]
    });

    this.title = this.data.name;
    switch (this.title) {
      case 'Добавление тренера':
        this.trainerForm.controls['id'].setValue(0);
        break;
      case 'Редактирование тренера':
        this.showId = true;
        break;
      case 'Тренер':
        this.showId = true;
        this.onlyView = true;
        this.trainerForm.disable();
        break;
    }
  }

  submit() {
    if (this.trainerForm.invalid) return;
    this.dialogRef.close(this.trainerForm.value);
  }

  cancel() {
    this.dialogRef.close();
  }
}