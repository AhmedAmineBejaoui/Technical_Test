import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { LoginRequest, SignupRequest, AuthResponse, StoredUser } from '../../models/auth.model';
import { Role } from '../../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  
  private readonly API_URL = 'http://localhost:8080/api';
  private readonly TOKEN_KEY = 'auth_token';
  private readonly USER_KEY = 'auth_user';
  
  private currentUserSubject = new BehaviorSubject<StoredUser | null>(this.getUserFromStorage());
  public currentUser$ = this.currentUserSubject.asObservable();

  /**
   * Login with email and password
   */
  login(email: string, password: string): Observable<AuthResponse> {
    const credentials: LoginRequest = { email, password };
    return this.http.post<AuthResponse>(`${this.API_URL}/auth/login`, credentials).pipe(
      tap(response => this.handleAuthSuccess(response))
    );
  }

  /**
   * Signup a new client
   */
  signup(data: SignupRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/auth/signup`, data).pipe(
      tap(response => this.handleAuthSuccess(response))
    );
  }

  /**
   * Logout and clear storage
   */
  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  /**
   * Get stored JWT token
   */
  getToken(): string | null {
    if (typeof localStorage === 'undefined') return null;
    return localStorage.getItem(this.TOKEN_KEY);
  }

  /**
   * Check if user is logged in
   */
  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  /**
   * Get current user's role
   */
  getRole(): Role | null {
    const user = this.getCurrentUser();
    return user?.role ?? null;
  }

  /**
   * Get current user from BehaviorSubject
   */
  getCurrentUser(): StoredUser | null {
    return this.currentUserSubject.value;
  }

  /**
   * Check if user is admin
   */
  isAdmin(): boolean {
    return this.getRole() === Role.ADMIN;
  }

  /**
   * Check if user is client
   */
  isClient(): boolean {
    return this.getRole() === Role.CLIENT;
  }

  /**
   * Redirect user based on role after login
   */
  redirectBasedOnRole(): void {
    if (this.isAdmin()) {
      this.router.navigate(['/admin/users']);
    } else if (this.isClient()) {
      this.router.navigate(['/client/profile']);
    } else {
      this.router.navigate(['/login']);
    }
  }

  /**
   * Handle successful authentication
   */
  private handleAuthSuccess(response: AuthResponse): void {
    const storedUser: StoredUser = {
      id: response.user.id,
      email: response.user.email,
      firstName: response.user.firstName,
      lastName: response.user.lastName,
      role: response.user.role
    };
    
    localStorage.setItem(this.TOKEN_KEY, response.token);
    localStorage.setItem(this.USER_KEY, JSON.stringify(storedUser));
    this.currentUserSubject.next(storedUser);
  }

  /**
   * Get user from localStorage
   */
  private getUserFromStorage(): StoredUser | null {
    if (typeof localStorage === 'undefined') return null;
    const userJson = localStorage.getItem(this.USER_KEY);
    return userJson ? JSON.parse(userJson) : null;
  }
}
