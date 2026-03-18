import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, combineLatest, Observable } from 'rxjs';
import { map, startWith, switchMap, shareReplay } from 'rxjs/operators';
import { Activity } from '../../../core/interfaces/activity';
import { Review } from '../../../core/interfaces/review';
import { User } from '../../../core/interfaces/user';
import { DataStorageService } from '../../../core/services/data-storage.service';
import { ActivityService } from '../../../core/services/activity.service';
import { ReviewService } from '../../../core/services/review.service';
import { UserService } from '../../../core/services/user.service';
import { CustomToastrService } from '../../../core/services/toastr.service';
import { MatDialog } from '@angular/material/dialog';
import { EditReviewComponent } from '../edit-review/edit-review.component';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-review-list',
  standalone: false,
  templateUrl: './review-list.component.html',
  styleUrl: './review-list.component.css',
})
export class ReviewListComponent implements OnInit {
  role: string = '';
  userId = 0;
  activityId = 0;

  user?: User;
  activity!: Activity;
  userReview?: Review;

  private search$ = new BehaviorSubject<string>('');
  private refresh$ = new BehaviorSubject<void>(undefined);

  reviews$: Observable<Review[]> = this.refresh$.pipe(
    switchMap(() => this.reviewService.findByActivityId(this.activityId).pipe(
      map(rev => {
        if (this.role === 'USER') {
          this.userReview = rev.find(r => r.userId === this.userId);
        }
        return rev;
      })
    )),
    startWith([]),
    shareReplay(1)
  );

  filteredReviews$: Observable<Review[]> = combineLatest([this.reviews$, this.search$])
    .pipe(map(([reviews, search]) =>
      reviews.filter(r =>
        r.registrationActivityName.toUpperCase().includes(search.toUpperCase())
      )
    ));

  constructor(
    private dataStorage: DataStorageService,
    private activityService: ActivityService,
    private reviewService: ReviewService,
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
      this.userService.findById(this.userId).subscribe(u => (this.user = u));
    }

    this.activityId = Number(this.route.snapshot.paramMap.get('activityId'));
    if (this.activityId) {
      this.activityService.findById(this.activityId).subscribe(a => (this.activity = a));
    }
  }

  onSearch(value: string) {
    this.search$.next(value);
  }

  openDialog(title: string, review?: Review) {
    const dialogRef = this.dialog.open(EditReviewComponent, {
      data: {
        name: title,
        review: review
          ? { ...review }
          : {
            id: 0,
            userId: this.user?.id ?? 0,
            userName: this.user?.fullName ?? '',
            registrationId: 0,
            registrationActivityName: this.activity.name,
            rating: 0,
            comment: ''
          },
        activityId: this.activityId,
        userId: this.userId
      }
    });

    return dialogRef.afterClosed();
  }

  create() {
    this.openDialog('Добавление отзыва').subscribe(result => {
      if (!result) return;

      this.reviewService.create(result).subscribe(() => {
        this.toast.success('Успешно', 'Отзыв добавлен!');
        this.refresh$.next();
      });
    });
  }

  update(review: Review) {
    this.openDialog('Редактирование отзыва', review).subscribe(result => {
      if (!result) return;

      this.reviewService.update(result.id, result).subscribe(() => {
        this.toast.success('Успешно', 'Отзыв изменён!');
        this.refresh$.next();
      });
    });
  }

  delete(review: Review) {
    if (!confirm(
      `Удалить отзыв пользователя "${review.userName}" для активности "${review.registrationActivityName}"?`
    )) return;

    this.reviewService.delete(review.id).subscribe(() => {
      this.toast.success('Успешно', 'Отзыв удалён!');
      this.refresh$.next();
    });
  }
}