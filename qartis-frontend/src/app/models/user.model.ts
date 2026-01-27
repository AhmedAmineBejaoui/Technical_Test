export enum Role {
  ADMIN = 'ADMIN',
  CLIENT = 'CLIENT'
}

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  role: Role;
  isActive: boolean;
  companyId?: number;
  companyName?: string;
  company?: Company;
}

export interface Company {
  id: number;
  name: string;
  address?: string;
  taxNumber?: string;
  phone?: string;
  createdAt?: string;
  updatedAt?: string;
}

// DTO for creating a user
export interface UserCreateRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  role: Role;
  companyId?: number;
}

// DTO for updating a user
export interface UserUpdateRequest {
  firstName: string;
  lastName: string;
  email: string;
  role: Role;
  companyId?: number;
  isActive?: boolean;
}

// Paginated response
export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

// Filter params for users list
export interface UserFilterParams {
  page?: number;
  size?: number;
  email?: string;
  role?: Role;
  name?: string;
  company?: string;
  active?: boolean;
  sortBy?: string;
  sortDirection?: 'asc' | 'desc';
}
