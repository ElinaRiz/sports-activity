import { Component, OnInit, Inject } from '@angular/core';
import { ActivityType } from '../../../core/interfaces/activity-type';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-edit-activity-type',
  standalone: false,
  templateUrl: './edit-activity-type.component.html',
  styleUrl: './edit-activity-type.component.css',
})
export class EditActivityTypeComponent implements OnInit {
  title = '';
  activityTypeForm!: FormGroup;
  showId = false;
  onlyView = false;

  constructor(
    private formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA)
    public data: { name: string; activityType: ActivityType },
    public dialogRef: MatDialogRef<EditActivityTypeComponent>
  ) { }

  ngOnInit() {
    const a = this.data.activityType;
    this.activityTypeForm = this.formBuilder.group({
      id: [a.id],
      name: [a.name, [Validators.required]],
      baseDuration: [a.baseDuration, [Validators.required, Validators.min(1)]],
      baseCost: [a.baseCost, [Validators.required, Validators.min(0)]]
    });

    this.title = this.data.name;
    switch (this.title) {
      case 'Добавление вида занятий':
        this.activityTypeForm.controls['id'].setValue(0);
        break;
      case 'Редактирование вида занятий':
        this.showId = true;
        break;
      case 'Вид занятий':
        this.showId = true;
        this.onlyView = true;
        this.activityTypeForm.disable();
        break;
    }
  }

  submit() {
    if (this.activityTypeForm.invalid) return;
    this.dialogRef.close(this.activityTypeForm.value);
  }

  cancel() {
    this.dialogRef.close();
  }
}