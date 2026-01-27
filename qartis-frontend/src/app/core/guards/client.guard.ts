import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../auth/auth.service';

/**
 * ClientGuard: Requires CLIENT role
 */
export const clientGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isLoggedIn()) {
    router.navigate(['/login']);
    return false;
  }

  if (authService.isClient()) {
    return true;
  }

  // If admin, redirect to admin area
  if (authService.isAdmin()) {
    router.navigate(['/admin/users']);
  } else {
    router.navigate(['/login']);
  }
  return false;
};
