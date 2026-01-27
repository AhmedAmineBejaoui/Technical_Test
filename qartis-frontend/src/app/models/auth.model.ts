import { User, Role } from './user.model';

// ============ Request DTOs ============

export interface LoginRequest {
  email: string;
  password: string;
}

export interface SignupRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  companyName?: string;
}

// ============ Response DTOs ============

export interface AuthResponse {
  token: string;
  user: UserDto;
}

export interface UserDto {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  role: Role;
  isActive?: boolean;
}

// ============ Error Response DTOs ============

export interface BilingualMessage {
  en: string;
  ar: string;
}

export interface ApiErrorResponse {
  message: string | BilingualMessage;
  status?: number;
  timestamp?: string;
  path?: string;
}

// ============ Stored User ============

export interface StoredUser {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  role: Role;
  companyId?: number;
  companyName?: string;
  company?: {
    id: number;
    name: string;
    address?: string;
  };
}

