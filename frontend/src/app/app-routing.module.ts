import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RoleGuard } from './core/guards/role.guard';
import { NoAuthGuard } from './core/guards/no-auth.guard';

import { ActivityListComponent } from './pages/activities/activity-list/activity-list.component';
import { ActivityTypeListComponent } from './pages/activity-types/activity-type-list/activity-type-list.component';
import { HallListComponent } from './pages/halls/hall-list/hall-list.component';
import { RegistrationListComponent } from './pages/registrations/registration-list/registration-list.component';
import { ReviewListComponent } from './pages/reviews/review-list/review-list.component';
import { TrainerListComponent } from './pages/trainers/trainer-list/trainer-list.component';
import { UserListComponent } from './pages/users/user-list/user-list.component';
import { LoginComponent } from './pages/main/login/login.component';
import { NotFoundComponent } from './pages/main/not-found/not-found.component';
import { ProfileComponent } from './pages/main/profile/profile.component';
import { RegisterComponent } from './pages/main/register/register.component';
import { ForbiddenComponent } from './pages/main/forbidden/forbidden.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [NoAuthGuard]
  },
  {
    path: 'register',
    component: RegisterComponent,
    canActivate: [NoAuthGuard]
  },
  {
    path: 'profile',
    component: ProfileComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ADMIN', 'TRAINER', 'USER'] }
  },
  {
    path: 'activities',
    component: ActivityListComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ADMIN', 'TRAINER', 'USER'] }
  },
  {
    path: 'activity-types',
    component: ActivityTypeListComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ADMIN', 'TRAINER', 'USER'] }
  },
  {
    path: 'halls',
    component: HallListComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ADMIN', 'TRAINER'] }
  },
  {
    path: 'activities/:activityId/registrations',
    component: RegistrationListComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ADMIN', 'TRAINER', 'USER'] }
  },
  {
    path: 'activities/:activityId/reviews',
    component: ReviewListComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ADMIN', 'TRAINER', 'USER'] }
  },
  {
    path: 'trainers',
    component: TrainerListComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ADMIN', 'TRAINER', 'USER'] }
  },
  {
    path: 'users',
    component: UserListComponent,
    canActivate: [RoleGuard],
    data: { roles: ['ADMIN', 'TRAINER'] }
  },
  { path: 'forbidden', component: ForbiddenComponent },
  { path: '**', component: NotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
