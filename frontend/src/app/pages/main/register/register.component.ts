import { Component, OnInit } from '@angular/core';
import { ERole } from '../../../core/interfaces/user';
import { AuthService } from '../../../core/services/auth.service';
import { CustomToastrService } from '../../../core/services/toastr.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})

export class RegisterComponent implements OnInit {
  form: any = {
    login: '',
    fullName: '',
    email: '',
    password: '',
    confirmPassword: '',
    phone: null,
    birthDate: null
  };

  isPasswordNotEquals = false;
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private toast: CustomToastrService,
    private router: Router
  ) { }

  ngOnInit() { }

  onSubmit() {
    const { login, email, password, confirmPassword, fullName, phone, birthDate } = this.form;

    if (password !== confirmPassword) {
      this.isPasswordNotEquals = true;
      return;
    }

    const newUser = { login, email, password, confirmPassword, fullName, phone, role: ERole.USER, birthDate };

    this.isPasswordNotEquals = false;
    this.authService.register(newUser).subscribe(() => {
      this.toast.success('Успешно', 'Ваша регистрация прошла успешно!');
      this.router.navigate(['/login']);
    });
  }
}
