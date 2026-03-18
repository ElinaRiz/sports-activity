import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { DataStorageService } from '../services/data-storage.service';

@Injectable({
  providedIn: 'root',
})
export class NoAuthGuard implements CanActivate {

  constructor(private dataStorage: DataStorageService, private router: Router) { }

  canActivate(): boolean {
    const token = this.dataStorage.getToken();

    if (token) {
      this.router.navigate(['/forbidden']);
      return false;
    }

    return true;
  }
}