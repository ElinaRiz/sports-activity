import { Component, OnInit, Inject } from '@angular/core';
import { Review } from '../../../core/interfaces/review';
import { Registration } from '../../../core/interfaces/registration';
import { RegistrationService } from '../../../core/services/registration.service';
import { CustomToastrService } from '../../../core/services/toastr.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-edit-review',
  standalone: false,
  templateUrl: './edit-review.component.html',
  styleUrl: './edit-review.component.css',
})
export class EditReviewComponent implements OnInit {
  title = '';
  reviewForm!: FormGroup;
  showId = false;
  onlyView = false;

  registration: Registration[] = [];

  constructor(
    private registrationService: RegistrationService,
    private toast: CustomToastrService,
    private formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA)
    public data: { name: string; review: Review; activityId: number, userId: number },
    public dialogRef: MatDialogRef<EditReviewComponent>
  ) { }

  ngOnInit() {
    const activityId = this.data.activityId;
    const userId = this.data.userId;
    if (userId === 0) {
      this.registrationService.findByActivityId(activityId).subscribe(reg => {
        this.registration = reg;
        if (!reg.length) {
          this.toast.error('Ошибка', 'Необходимо добавить минимум одну регистрацию!');
          this.dialogRef.close();
        }
      });
    } else {
      this.registrationService.findByUserIdAndActivityId(userId, activityId).subscribe((reg) => {
        this.registration = [reg];
      })
    }

    const r = this.data.review;
    this.reviewForm = this.formBuilder.group({
      id: [r.id],
      userId: [r.userId],
      userName: [r.userName],
      registrationId: [r.registrationId, [Validators.required, Validators.min(1)]],
      registrationActivityName: [r.registrationActivityName],
      rating: [r.rating, [Validators.required, Validators.min(1), Validators.max(5)]],
      comment: [r.comment, [Validators.maxLength(500)]]
    });

    this.title = this.data.name;
    switch (this.title) {
      case 'Добавление отзыва':
        this.reviewForm.controls['id'].setValue(0);
        break;
      case 'Редактирование отзыва':
        this.showId = true;
        break;
      case 'Отзыв':
        this.showId = true;
        this.onlyView = true;
        this.reviewForm.disable();
        break;
    }
  }

  submit() {
    if (this.reviewForm.invalid) return;
    this.dialogRef.close(this.reviewForm.value);
  }

  cancel() {
    this.dialogRef.close();
  }
}