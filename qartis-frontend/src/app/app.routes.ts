import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/admin.guard';
import { clientGuard } from './core/guards/client.guard';

export const routes: Routes = [
  // ============ Public Routes ============
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'signup',
    loadComponent: () => import('./features/auth/signup/signup.component').then(m => m.SignupComponent)
  },

  // ============ Admin Routes (ADMIN role required) ============
  {
    path: 'admin',
    canActivate: [authGuard, adminGuard],
    children: [
      {
        path: 'users',
        loadComponent: () => import('./features/admin/users/users.component').then(m => m.UsersComponent)
      },
      {
        path: 'companies/:id',
        loadComponent: () => import('./features/admin/companies/company-detail.component').then(m => m.CompanyDetailComponent)
      },
      {
        path: '',
        redirectTo: 'users',
        pathMatch: 'full'
      }
    ]
  },

  // ============ Client Routes (CLIENT role required) ============
  {
    path: 'client',
    canActivate: [authGuard, clientGuard],
    children: [
      {
        path: 'profile',
        loadComponent: () => import('./features/client/profile/profile.component').then(m => m.ProfileComponent)
      },
      {
        path: '',
        redirectTo: 'profile',
        pathMatch: 'full'
      }
    ]
  },

  // ============ Fallback ============
  {
    path: '**',
    redirectTo: '/login'
  }
];

