import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User, UserCreateRequest, UserUpdateRequest, PageResponse, UserFilterParams } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8080/api';

  /**
   * Get paginated users with filters (Admin)
   */
  getUsers(params: UserFilterParams): Observable<PageResponse<User>> {
    let httpParams = new HttpParams();
    
    if (params.page !== undefined) httpParams = httpParams.set('page', params.page.toString());
    if (params.size !== undefined) httpParams = httpParams.set('size', params.size.toString());
    if (params.email) httpParams = httpParams.set('email', params.email);
    if (params.role) httpParams = httpParams.set('role', params.role);
    if (params.name) httpParams = httpParams.set('name', params.name);
    if (params.company) httpParams = httpParams.set('company', params.company);
    if (params.active !== undefined) httpParams = httpParams.set('active', params.active.toString());
    if (params.sortBy) httpParams = httpParams.set('sortBy', params.sortBy);
    if (params.sortDirection) httpParams = httpParams.set('sortDirection', params.sortDirection);

    return this.http.get<PageResponse<User>>(`${this.API_URL}/admin/users`, { params: httpParams });
  }

  /**
   * Get all users (legacy - no pagination)
   */
  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.API_URL}/admin/users`);
  }

  /**
   * Get user by ID
   */
  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.API_URL}/admin/users/${id}`);
  }

  /**
   * Create new user (Admin)
   */
  createUser(user: UserCreateRequest): Observable<User> {
    return this.http.post<User>(`${this.API_URL}/admin/users`, user);
  }

  /**
   * Update existing user (Admin)
   */
  updateUser(id: number, user: UserUpdateRequest): Observable<User> {
    return this.http.put<User>(`${this.API_URL}/admin/users/${id}`, user);
  }

  /**
   * Delete user (Admin)
   */
  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/admin/users/${id}`);
  }

  /**
   * Toggle user active status (Admin)
   */
  toggleUserStatus(id: number): Observable<User> {
    return this.http.patch<User>(`${this.API_URL}/admin/users/${id}/toggle-status`, {});
  }

  /**
   * Get current user profile (Client)
   */
  getProfile(): Observable<User> {
    return this.http.get<User>(`${this.API_URL}/client/me`);
  }

  /**
   * Update current user profile (Client)
   */
  updateProfile(data: any): Observable<User> {
    return this.http.put<User>(`${this.API_URL}/client/me`, data);
  }
}
