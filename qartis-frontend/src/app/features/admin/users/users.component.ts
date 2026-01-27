import { Component, OnInit, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TableModule, Table, TableLazyLoadEvent } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { DialogModule } from 'primeng/dialog';
import { PasswordModule } from 'primeng/password';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ToolbarModule } from 'primeng/toolbar';
import { ToggleSwitchModule } from 'primeng/toggleswitch';
import { SkeletonModule } from 'primeng/skeleton';

import { UserService } from '../../../services/user.service';
import { CompanyService } from '../../../services/company.service';
import { AuthService } from '../../../core/auth/auth.service';
import { User, Role, UserCreateRequest, UserUpdateRequest, Company } from '../../../models/user.model';
import { BilingualMessage } from '../../../models/auth.model';
import AOS from 'aos';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    TableModule,
    ButtonModule,
    CardModule,
    TagModule,
    TooltipModule,
    InputTextModule,
    SelectModule,
    DialogModule,
    PasswordModule,
    ConfirmDialogModule,
    ToolbarModule,
    ToggleSwitchModule,
    SkeletonModule
  ],
  providers: [ConfirmationService],
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  @ViewChild('dt') table!: Table;

  private readonly userService = inject(UserService);
  private readonly companyService = inject(CompanyService);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly messageService = inject(MessageService);

  // Table data
  users: User[] = [];
  totalRecords = 0;
  loading = false;
  rows = 10;

  // Filters
  filterEmail = '';
  filterName = '';
  filterCompany = '';
  filterRole: Role | null = null;

  // Role options for dropdown
  roleOptions = [
    { label: 'Tous', value: null },
    { label: 'Admin', value: Role.ADMIN },
    { label: 'Client', value: Role.CLIENT }
  ];

  // Dialog
  userDialog = false;
  isEditMode = false;
  userForm!: FormGroup;
  submitting = false;

  // Companies for dropdown
  companies: Company[] = [];

  ngOnInit(): void {
    AOS.init({ duration: 800, once: true });
    this.initForm();
    this.loadCompanies();
  }

  initForm(): void {
    this.userForm = this.fb.group({
      id: [null],
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      role: [Role.CLIENT, Validators.required],
      companyId: [null],
      isActive: [true]
    });
  }

  loadCompanies(): void {
    this.companyService.getAllCompanies().subscribe({
      next: (companies) => this.companies = companies,
      error: () => {} // Silently fail if no companies endpoint
    });
  }

  /**
   * Lazy load users from backend
   */
  loadUsers(event: TableLazyLoadEvent): void {
    this.loading = true;

    const page = event.first ? Math.floor(event.first / (event.rows || this.rows)) : 0;
    const size = event.rows || this.rows;

    // Sort
    let sortBy = 'id';
    let sortDirection: 'asc' | 'desc' = 'asc';
    if (event.sortField) {
      sortBy = event.sortField as string;
      sortDirection = event.sortOrder === 1 ? 'asc' : 'desc';
    }

    this.userService.getUsers({
      page,
      size,
      email: this.filterEmail || undefined,
      name: this.filterName || undefined,
      company: this.filterCompany || undefined,
      role: this.filterRole || undefined,
      sortBy,
      sortDirection
    }).subscribe({
      next: (response) => {
        this.users = response.content;
        this.totalRecords = response.totalElements;
        this.loading = false;
      },
      error: (error) => {
        this.loading = false;
        this.showErrorToast(error);
      }
    });
  }

  /**
   * Apply filters and reload table
   */
  applyFilters(): void {
    this.table.first = 0; // Reset to first page
    this.table._filter();
  }

  /**
   * Clear all filters
   */
  clearFilters(): void {
    this.filterEmail = '';
    this.filterName = '';
    this.filterCompany = '';
    this.filterRole = null;
    this.applyFilters();
  }

  /**
   * Navigate to company detail
   */
  goToCompany(companyId: number): void {
    this.router.navigate(['/admin/companies', companyId]);
  }

  /**
   * Open dialog to create new user
   */
  openNewUserDialog(): void {
    this.isEditMode = false;
    this.userForm.reset({
      role: Role.CLIENT,
      isActive: true
    });
    // Password required for new user
    this.userForm.get('password')?.setValidators([Validators.required, Validators.minLength(8)]);
    this.userForm.get('password')?.updateValueAndValidity();
    this.userDialog = true;
  }

  /**
   * Open dialog to edit existing user
   */
  openEditUserDialog(user: User): void {
    this.isEditMode = true;
    this.userForm.patchValue({
      id: user.id,
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      role: user.role,
      companyId: user.companyId,
      isActive: user.isActive
    });
    // Password optional for edit
    this.userForm.get('password')?.clearValidators();
    this.userForm.get('password')?.updateValueAndValidity();
    this.userDialog = true;
  }

  /**
   * Close dialog
   */
  closeDialog(): void {
    this.userDialog = false;
    this.userForm.reset();
  }

  /**
   * Save user (create or update)
   */
  saveUser(): void {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }

    this.submitting = true;
    const formValue = this.userForm.value;

    if (this.isEditMode) {
      // Update
      const updateData: UserUpdateRequest = {
        firstName: formValue.firstName,
        lastName: formValue.lastName,
        email: formValue.email,
        role: formValue.role,
        companyId: formValue.companyId,
        isActive: formValue.isActive
      };

      this.userService.updateUser(formValue.id, updateData).subscribe({
        next: () => {
          this.submitting = false;
          this.closeDialog();
          this.messageService.add({
            severity: 'success',
            summary: 'Succès / Success',
            detail: 'Utilisateur mis à jour / User updated',
            life: 3000
          });
          this.table._filter(); // Reload
        },
        error: (error) => {
          this.submitting = false;
          this.showErrorToast(error);
        }
      });
    } else {
      // Create
      const createData: UserCreateRequest = {
        firstName: formValue.firstName,
        lastName: formValue.lastName,
        email: formValue.email,
        password: formValue.password,
        role: formValue.role,
        companyId: formValue.companyId
      };

      this.userService.createUser(createData).subscribe({
        next: () => {
          this.submitting = false;
          this.closeDialog();
          this.messageService.add({
            severity: 'success',
            summary: 'Succès / Success',
            detail: 'Utilisateur créé / User created',
            life: 3000
          });
          this.table._filter(); // Reload
        },
        error: (error) => {
          this.submitting = false;
          this.showErrorToast(error);
        }
      });
    }
  }

  /**
   * Confirm and delete user
   */
  confirmDelete(user: User): void {
    this.confirmationService.confirm({
      message: `Êtes-vous sûr de vouloir supprimer ${user.firstName} ${user.lastName} ?`,
      header: 'Confirmation de suppression',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Oui, supprimer',
      rejectLabel: 'Annuler',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.userService.deleteUser(user.id).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Supprimé / Deleted',
              detail: 'Utilisateur supprimé / User deleted',
              life: 3000
            });
            this.table._filter(); // Reload
          },
          error: (error) => this.showErrorToast(error)
        });
      }
    });
  }

  /**
   * Toggle user active status
   */
  toggleStatus(user: User): void {
    this.userService.toggleUserStatus(user.id).subscribe({
      next: (updatedUser) => {
        user.isActive = updatedUser.isActive;
        this.messageService.add({
          severity: 'info',
          summary: 'Statut modifié',
          detail: updatedUser.isActive ? 'Utilisateur activé' : 'Utilisateur désactivé',
          life: 3000
        });
      },
      error: (error) => this.showErrorToast(error)
    });
  }

  /**
   * Get role badge severity
   */
  getRoleSeverity(role: Role): 'success' | 'info' | 'warn' | 'danger' | 'secondary' | 'contrast' {
    return role === Role.ADMIN ? 'danger' : 'info';
  }

  /**
   * Get status badge severity
   */
  getStatusSeverity(isActive: boolean): 'success' | 'danger' {
    return isActive ? 'success' : 'danger';
  }

  /**
   * Show error toast with bilingual support
   */
  private showErrorToast(error: any): void {
    const message = error.error?.message;
    
    if (this.isBilingualMessage(message)) {
      this.messageService.add({
        severity: 'error',
        summary: message.en,
        detail: message.ar,
        life: 5000
      });
    } else if (typeof message === 'string') {
      this.messageService.add({
        severity: 'error',
        summary: 'Erreur / Error',
        detail: message,
        life: 5000
      });
    } else {
      this.messageService.add({
        severity: 'error',
        summary: 'Erreur / Error',
        detail: 'Une erreur est survenue',
        life: 5000
      });
    }
  }

  private isBilingualMessage(obj: any): obj is BilingualMessage {
    return obj && typeof obj === 'object' && 'en' in obj && 'ar' in obj;
  }

  logout(): void {
    this.authService.logout();
  }
}
