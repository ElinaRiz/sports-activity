import { Component, OnInit, Inject } from '@angular/core';
import { Hall } from '../../../core/interfaces/hall';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-edit-hall',
  standalone: false,
  templateUrl: './edit-hall.component.html',
  styleUrl: './edit-hall.component.css',
})
export class EditHallComponent implements OnInit {
  title = '';
  hallForm!: FormGroup;
  showId = false;
  onlyView = false;

  constructor(
    private formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA)
    public data: { name: string; hall: Hall },
    public dialogRef: MatDialogRef<EditHallComponent>
  ) { }

  ngOnInit() {
    const h = this.data.hall;
    this.hallForm = this.formBuilder.group({
      id: [h.id],
      name: [h.name, [Validators.required]],
      room: [h.room, [Validators.required, Validators.min(1)]],
      capacity: [h.capacity, [Validators.required, Validators.min(1)]]
    });

    this.title = this.data.name;
    switch (this.title) {
      case 'Добавление зала':
        this.hallForm.controls['id'].setValue(0);
        break;
      case 'Редактирование зала':
        this.showId = true;
        break;
      case 'Зал':
        this.showId = true;
        this.onlyView = true;
        this.hallForm.disable();
        break;
    }
  }

  submit() {
    if (this.hallForm.invalid) return;
    this.dialogRef.close(this.hallForm.value);
  }

  cancel() {
    this.dialogRef.close();
  }
}