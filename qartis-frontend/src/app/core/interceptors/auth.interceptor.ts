import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { MessageService } from 'primeng/api';
import { AuthService } from '../auth/auth.service';
import { ApiErrorResponse, BilingualMessage } from '../../models/auth.model';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const messageService = inject(MessageService);
  const router = inject(Router);
  
  const token = authService.getToken();

  // Clone request with Authorization header if token exists
  let authReq = req;
  if (token) {
    authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      // Handle 401 Unauthorized
      if (error.status === 401) {
        authService.logout();
        router.navigate(['/login']);
        showErrorToast(messageService, error, 'Session expirée. Veuillez vous reconnecter.');
        return throwError(() => error);
      }

      // Handle other errors with toast
      showErrorToast(messageService, error);
      return throwError(() => error);
    })
  );
};

/**
 * Extract error message and display toast
 * Supports bilingual messages {en, ar}
 */
function showErrorToast(
  messageService: MessageService, 
  error: HttpErrorResponse,
  defaultMessage?: string
): void {
  let summary = 'Erreur';
  let detailEn = defaultMessage || 'Une erreur est survenue';
  let detailAr = '';

  const errorBody = error.error as ApiErrorResponse;

  if (errorBody?.message) {
    if (typeof errorBody.message === 'string') {
      detailEn = errorBody.message;
    } else if (isBilingualMessage(errorBody.message)) {
      detailEn = errorBody.message.en;
      detailAr = errorBody.message.ar;
    }
  }

  // Show English message
  messageService.add({
    severity: 'error',
    summary: summary,
    detail: detailEn,
    life: 5000
  });

  // Show Arabic message if present
  if (detailAr) {
    messageService.add({
      severity: 'error',
      summary: 'خطأ',
      detail: detailAr,
      life: 5000
    });
  }
}

/**
 * Type guard for BilingualMessage
 */
function isBilingualMessage(message: unknown): message is BilingualMessage {
  return (
    typeof message === 'object' &&
    message !== null &&
    'en' in message &&
    'ar' in message
  );
}
