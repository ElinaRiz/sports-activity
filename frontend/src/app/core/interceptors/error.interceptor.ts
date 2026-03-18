import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { CustomToastrService } from '../services/toastr.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(private toast: CustomToastrService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {

        if (error.status === 0) {
          this.toast.warning('Предупреждение', 'Сервер не работает!');
        }
        else if (error.status === 403) {
          this.toast.error('Ошибка доступа', 'Недостаточно прав!');
        }
        else if (error.status >= 500) {
          this.toast.error('Ошибка сервера', 'Попробуйте позже');
        }
        else {
          this.toast.error('Ошибка', error.error);
        }

        return throwError(() => error);
      })
    );
  }
}