import { Component, OnInit, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { SkeletonModule } from 'primeng/skeleton';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { DividerModule } from 'primeng/divider';
import { TagModule } from 'primeng/tag';
import { UserService } from '../../../services/user.service';
import { CompanyService } from '../../../services/company.service';
import { AuthService } from '../../../core/auth/auth.service';
import { StoredUser } from '../../../models/auth.model';
import { User, Company } from '../../../models/user.model';
import { CompanyModalComponent } from '../../../shared/components/company-modal/company-modal.component';
import AOS from 'aos';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CardModule,
    InputTextModule,
    ButtonModule,
    MessageModule,
    SkeletonModule,
    ToastModule,
    DividerModule,
    TagModule,
    CompanyModalComponent
  ],
  providers: [MessageService],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  @ViewChild(CompanyModalComponent) companyModal!: CompanyModalComponent;

  private readonly fb = inject(FormBuilder);
  private readonly userService = inject(UserService);
  private readonly companyService = inject(CompanyService);
  private readonly authService = inject(AuthService);
  private readonly messageService = inject(MessageService);

  profileForm: FormGroup;
  editMode = false;
  profileLoading = true;
  companyLoading = false;
  submitting = false;
  user: User | null = null;
  company: Company | null = null;
  successMessage = '';
  errorMessage = '';
  companyModalVisible = false;

  constructor() {
    this.profileForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: [{ value: '', disabled: true }]
    });
  }

  ngOnInit(): void {
    AOS.init({ duration: 800, once: true });
    this.loadProfile();
  }

  loadProfile(): void {
    this.profileLoading = true;
    this.errorMessage = '';

    this.userService.getProfile().subscribe({
      next: (user) => {
        this.user = user;
        this.profileForm.patchValue({
          firstName: user.firstName,
          lastName: user.lastName,
          email: user.email
        });
        this.loadCompany();
        this.profileLoading = false;
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Erreur lors du chargement du profil';
        this.profileLoading = false;
      }
    });
  }

  loadCompany(): void {
    if (!this.user?.id) return;

    this.companyLoading = true;
    this.companyService.getMyCompany().subscribe({
      next: (company) => {
        this.company = company;
        this.companyLoading = false;
      },
      error: () => {
        // Pas de company associée
        this.company = null;
        this.companyLoading = false;
      }
    });
  }

  toggleEditMode(): void {
    this.editMode = !this.editMode;
    if (!this.editMode) {
      this.successMessage = '';
      this.errorMessage = '';
    }
  }

  onSubmit(): void {
    if (!this.profileForm.valid) return;

    this.submitting = true;
    this.successMessage = '';
    this.errorMessage = '';

    this.userService.updateProfile(this.profileForm.value).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Succès',
          detail: 'Profil mis à jour avec succès',
          life: 3000
        });
        this.loadProfile();
        this.editMode = false;
        this.submitting = false;
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Erreur lors de la mise à jour';
        this.submitting = false;
      }
    });
  }

  openCompanyModal(): void {
    this.companyModalVisible = true;
  }

  onCompanySuccess(company: Company): void {
    this.company = company;
    this.companyModalVisible = false;
    const message = this.company ? 'Entreprise mise à jour' : 'Entreprise créée';
    this.messageService.add({
      severity: 'success',
      summary: 'Succès',
      detail: message,
      life: 3000
    });
  }

  logout(): void {
    this.authService.logout();
  }
}
