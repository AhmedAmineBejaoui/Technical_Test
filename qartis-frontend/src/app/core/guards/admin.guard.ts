import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { Role } from '../../models/user.model';

/**
 * RoleGuard factory: Requires specific role to access route
 */
export const roleGuard = (allowedRoles: Role[]): CanActivateFn => {
  return (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    if (!authService.isLoggedIn()) {
      router.navigate(['/login']);
      return false;
    }

    const userRole = authService.getRole();
    if (userRole && allowedRoles.includes(userRole)) {
      return true;
    }

    // Redirect to appropriate page based on role
    authService.redirectBasedOnRole();
    return false;
  };
};

/**
 * AdminGuard: Requires ADMIN role
 */
export const adminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isLoggedIn()) {
    router.navigate(['/login']);
    return false;
  }

  if (authService.isAdmin()) {
    return true;
  }

  // If client, redirect to client area
  if (authService.isClient()) {
    router.navigate(['/client/profile']);
  } else {
    router.navigate(['/login']);
  }
  return false;
};
