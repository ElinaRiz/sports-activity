import { Component, OnInit } from '@angular/core';
import { DataStorageService } from '../../../core/services/data-storage.service';
import { AuthService } from '../../../core/services/auth.service';
import { CustomToastrService } from '../../../core/services/toastr.service';
import { AppComponent } from '../../../app.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent implements OnInit {
  form: any = {
    login: null,
    password: null,
  };
  loginUser = '';
  fieldPasswordTextType = false;

  constructor(
    private dataStorage: DataStorageService,
    private authService: AuthService,
    private toast: CustomToastrService,
    private appComponent: AppComponent,
    private router: Router
  ) { }

  ngOnInit() {
    if (this.dataStorage.getToken()) {
      this.loginUser = this.dataStorage.getLogin();
    }
  }

  toggleFieldPasswordTextType() {
    this.fieldPasswordTextType = !this.fieldPasswordTextType;
  }

  onSubmit() {
    const { login, password } = this.form;

    this.authService.login({ login, password }).subscribe((response) => {
      this.dataStorage.saveData(login, response.userId, response.token, response.role);
      this.loginUser = this.dataStorage.getLogin();

      this.toast.success('Добро пожаловать', 'Вход выполнен успешно!');
      this.appComponent.setBaseParam();

      this.router.navigate(['/profile']);
    });
  }
}
