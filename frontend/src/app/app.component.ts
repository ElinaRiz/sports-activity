import { Component, signal } from '@angular/core';
import { DataStorageService } from './core/services/data-storage.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent {
  protected readonly title = signal('frontend');

  role = '';
  username?: string;

  constructor(private dataStorage: DataStorageService) {
    this.setBaseParam();
  }

  setBaseParam(): void {
    this.role = this.dataStorage.getRole();
    this.username = this.dataStorage.getLogin();
  }

  logout(): void {
    this.dataStorage.signOut();
    window.location.reload();
  }
}