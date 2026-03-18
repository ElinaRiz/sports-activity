import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './core/interceptors/auth.interceptor';
import { ErrorInterceptor } from './core/interceptors/error.interceptor';

import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDialogModule } from '@angular/material/dialog';
import { MatTabsModule } from '@angular/material/tabs';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { ActivityListComponent } from './pages/activities/activity-list/activity-list.component';
import { EditActivityComponent } from './pages/activities/edit-activity/edit-activity.component';
import { ActivityTypeListComponent } from './pages/activity-types/activity-type-list/activity-type-list.component';
import { EditActivityTypeComponent } from './pages/activity-types/edit-activity-type/edit-activity-type.component';
import { HallListComponent } from './pages/halls/hall-list/hall-list.component';
import { EditHallComponent } from './pages/halls/edit-hall/edit-hall.component';
import { RegistrationListComponent } from './pages/registrations/registration-list/registration-list.component';
import { EditRegistrationComponent } from './pages/registrations/edit-registration/edit-registration.component';
import { ReviewListComponent } from './pages/reviews/review-list/review-list.component';
import { EditReviewComponent } from './pages/reviews/edit-review/edit-review.component';
import { TrainerListComponent } from './pages/trainers/trainer-list/trainer-list.component';
import { EditTrainerComponent } from './pages/trainers/edit-trainer/edit-trainer.component';
import { UserListComponent } from './pages/users/user-list/user-list.component';
import { EditUserComponent } from './pages/users/edit-user/edit-user.component';
import { LoginComponent } from './pages/main/login/login.component';
import { NotFoundComponent } from './pages/main/not-found/not-found.component';
import { ProfileComponent } from './pages/main/profile/profile.component';
import { RegisterComponent } from './pages/main/register/register.component';
import { ForbiddenComponent } from './pages/main/forbidden/forbidden.component';

@NgModule({
  declarations: [
    AppComponent,
    ActivityListComponent,
    EditActivityComponent,
    ActivityTypeListComponent,
    EditActivityTypeComponent,
    HallListComponent,
    EditHallComponent,
    RegistrationListComponent,
    EditRegistrationComponent,
    ReviewListComponent,
    EditReviewComponent,
    TrainerListComponent,
    EditTrainerComponent,
    UserListComponent,
    EditUserComponent,
    LoginComponent,
    NotFoundComponent,
    ProfileComponent,
    RegisterComponent,
    ForbiddenComponent
  ],
  imports: [
    BrowserModule,
    CommonModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    MatTabsModule,
    MatTableModule,
    MatButtonModule,
    MatNativeDateModule,
    MatDatepickerModule,
    MatSelectModule,
    MatDialogModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      timeOut: 5000,
      positionClass: 'toast-top-right',
      preventDuplicates: true,
      progressBar: true,
    })
  ],
  providers: [provideBrowserGlobalErrorListeners(),
  {
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true
  },
  {
    provide: HTTP_INTERCEPTORS,
    useClass: ErrorInterceptor,
    multi: true
  }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
