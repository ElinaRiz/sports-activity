import { Component, OnInit, Inject } from '@angular/core';
import { forkJoin } from 'rxjs';
import { ActivityType } from '../../../core/interfaces/activity-type';
import { Activity } from '../../../core/interfaces/activity';
import { Hall } from '../../../core/interfaces/hall';
import { Trainer } from '../../../core/interfaces/trainer';
import { ActivityTypeService } from '../../../core/services/activity-type.service';
import { HallService } from '../../../core/services/hall.service';
import { TrainerService } from '../../../core/services/trainer.service';
import { CustomToastrService } from '../../../core/services/toastr.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-edit-activity',
  standalone: false,
  templateUrl: './edit-activity.component.html',
  styleUrl: './edit-activity.component.css',
})
export class EditActivityComponent implements OnInit {
  title = '';
  activityForm!: FormGroup;
  showId = false;
  onlyView = false;

  activityTypes: ActivityType[] = [];
  trainers: Trainer[] = [];
  halls: Hall[] = [];
  activityLevels = ['Начальный', 'Средний', 'Продвинутый'];
  statusList = ['Запланировано', 'Проведено', 'Отменено'];

  constructor(
    private activityTypeService: ActivityTypeService,
    private hallService: HallService,
    private trainerService: TrainerService,
    private toast: CustomToastrService,
    private formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA)
    public data: { name: string; activity: Activity },
    public dialogRef: MatDialogRef<EditActivityComponent>
  ) { }

  ngOnInit() {
    forkJoin({
      reqOne: this.activityTypeService.findAll(),
      reqTwo: this.trainerService.findAll(),
      reqThree: this.hallService.findAll()
    }).subscribe(
      ({ reqOne, reqTwo, reqThree }) => {
        this.activityTypes = reqOne;
        this.trainers = reqTwo;
        this.halls = reqThree;
        if (this.activityTypes.length == 0) {
          this.toast.error('Ошибка', 'Необходимо добавить минимум один вид занятий!');
          this.dialogRef.close();
        }
        if (this.trainers.length == 0) {
          this.toast.error('Ошибка', 'Необходимо добавить минимум одного тренера!');
          this.dialogRef.close();
        }
        if (this.halls.length == 0) {
          this.toast.error('Ошибка', 'Необходимо добавить минимум один зал!');
          this.dialogRef.close();
        }
      }
    );

    const a = this.data.activity;
    this.activityForm = this.formBuilder.group({
      id: [a.id],
      name: [a.name, [Validators.required]],
      activityTypeId: [a.activityTypeId, [Validators.required]],
      trainerId: [a.trainerId, [Validators.required]],
      hallId: [a.hallId, [Validators.required]],
      activityLevel: [a.activityLevel, [Validators.required]],
      startTime: [a.startTime, [Validators.required]],
      duration: [a.duration, [Validators.required, Validators.min(1)]],
      maxParticipants: [a.maxParticipants, [Validators.required, Validators.min(1)]],
      cost: [a.cost, [Validators.required, Validators.min(0)]],
      status: [a.status, [Validators.required]]
    });

    this.title = this.data.name;
    switch (this.title) {
      case 'Добавление занятия':
        this.activityForm.controls['id'].setValue(0);
        break;
      case 'Редактирование занятия':
        this.showId = true;
        break;
      case 'Занятие':
        this.showId = true;
        this.onlyView = true;
        this.activityForm.disable();
        break;
    }
  }

  submit() {
    if (this.activityForm.invalid) return;
    this.dialogRef.close(this.activityForm.value);
  }

  cancel() {
    this.dialogRef.close();
  }
}