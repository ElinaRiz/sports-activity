import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { DataStorageService } from '../services/data-storage.service';

@Injectable({
  providedIn: 'root',
})
export class RoleGuard implements CanActivate {

  constructor(private dataStorage: DataStorageService, private router: Router) { }

  canActivate(route: any): boolean {
    const expectedRoles: string[] = route.data['roles'];
    const userRole = this.dataStorage.getRole();

    if (!userRole) {
      this.router.navigate(['/login']);
      return false;
    }

    if (!expectedRoles.includes(userRole)) {
      this.router.navigate(['/forbidden']);
      return false;
    }

    return true;
  }
}