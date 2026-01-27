import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Company } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {
  private http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8080/api';

  /**
   * Get company by ID (Admin)
   */
  getCompanyById(id: number): Observable<Company> {
    return this.http.get<Company>(`${this.API_URL}/admin/companies/${id}`);
  }

  /**
   * Get all companies (Admin) - for dropdowns
   */
  getAllCompanies(): Observable<Company[]> {
    return this.http.get<Company[]>(`${this.API_URL}/admin/companies`);
  }

  /**
   * Get current user's company (Client)
   */
  getMyCompany(): Observable<Company> {
    return this.http.get<Company>(`${this.API_URL}/client/me/company`);
  }

  /**
   * Create company for current user (Client)
   */
  createCompany(company: Partial<Company>): Observable<Company> {
    return this.http.post<Company>(`${this.API_URL}/client/me/company`, company);
  }

  /**
   * Update current user's company (Client)
   */
  updateCompany(company: Partial<Company>): Observable<Company> {
    return this.http.put<Company>(`${this.API_URL}/client/me/company`, company);
  }
}
