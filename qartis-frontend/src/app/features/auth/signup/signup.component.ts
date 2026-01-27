import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { DividerModule } from 'primeng/divider';
import { MessageService } from 'primeng/api';
import { AuthService } from '../../../core/auth/auth.service';
import { BilingualMessage } from '../../../models/auth.model';
import AOS from 'aos';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    CardModule,
    InputTextModule,
    PasswordModule,
    ButtonModule,
    MessageModule,
    DividerModule
  ],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private messageService = inject(MessageService);

  signupForm: FormGroup;
  loading = false;
  errorMessage = '';

  constructor() {
    this.signupForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8), this.passwordStrengthValidator]],
      companyName: ['']
    });
  }

  ngOnInit(): void {
    AOS.init({ duration: 800, once: true });
    
    // If already logged in, redirect based on role
    if (this.authService.isLoggedIn()) {
      this.authService.redirectBasedOnRole();
    }
  }

  // Custom validator for password strength
  private passwordStrengthValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;
    
    const hasUpperCase = /[A-Z]/.test(value);
    const hasLowerCase = /[a-z]/.test(value);
    const hasNumeric = /[0-9]/.test(value);
    
    const valid = hasUpperCase && hasLowerCase && hasNumeric;
    return valid ? null : { passwordStrength: true };
  }

  onSubmit(): void {
    if (this.signupForm.invalid) {
      this.signupForm.markAllAsTouched();
      this.messageService.add({
        severity: 'warn',
        summary: 'Formulaire invalide',
        detail: 'Veuillez corriger les erreurs',
        life: 3000
      });
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.authService.signup(this.signupForm.value).subscribe({
      next: () => {
        this.loading = false;
        
        // Show success toast
        this.messageService.add({
          severity: 'success',
          summary: 'Inscription réussie !',
          detail: 'Votre compte a été créé avec succès',
          life: 3000
        });
        
        // Auto-login: redirect to client profile
        setTimeout(() => {
          this.authService.redirectBasedOnRole();
        }, 500);
      },
      error: (error) => {
        this.loading = false;
        this.handleSignupError(error);
      }
    });
  }

  private handleSignupError(error: any): void {
    const message = error.error?.message;
    
    if (this.isBilingualMessage(message)) {
      // Show bilingual toast (EN as summary, AR as detail)
      this.messageService.add({
        severity: 'error',
        summary: message.en,
        detail: message.ar,
        life: 5000
      });
      this.errorMessage = message.en;
    } else if (typeof message === 'string') {
      this.messageService.add({
        severity: 'error',
        summary: 'Erreur',
        detail: message,
        life: 5000
      });
      this.errorMessage = message;
    } else {
      const defaultMsg = 'Une erreur est survenue lors de l\'inscription';
      this.messageService.add({
        severity: 'error',
        summary: 'Erreur d\'inscription',
        detail: defaultMsg,
        life: 5000
      });
      this.errorMessage = defaultMsg;
    }
  }

  private isBilingualMessage(obj: any): obj is BilingualMessage {
    return obj && typeof obj === 'object' && 'en' in obj && 'ar' in obj;
  }

  // Getters for template validation
  get firstNameInvalid(): boolean {
    const ctrl = this.signupForm.get('firstName');
    return !!(ctrl?.invalid && ctrl?.touched);
  }

  get lastNameInvalid(): boolean {
    const ctrl = this.signupForm.get('lastName');
    return !!(ctrl?.invalid && ctrl?.touched);
  }

  get emailInvalid(): boolean {
    const ctrl = this.signupForm.get('email');
    return !!(ctrl?.invalid && ctrl?.touched);
  }

  get passwordInvalid(): boolean {
    const ctrl = this.signupForm.get('password');
    return !!(ctrl?.invalid && ctrl?.touched);
  }

  get passwordErrors(): string[] {
    const ctrl = this.signupForm.get('password');
    const errors: string[] = [];
    if (ctrl?.errors?.['required']) errors.push('Mot de passe requis');
    if (ctrl?.errors?.['minlength']) errors.push('Minimum 8 caractères');
    if (ctrl?.errors?.['passwordStrength']) errors.push('Doit contenir majuscule, minuscule et chiffre');
    return errors;
  }
}
