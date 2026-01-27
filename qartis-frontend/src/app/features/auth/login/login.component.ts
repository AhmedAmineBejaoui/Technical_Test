import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';
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
  selector: 'app-login',
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
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private messageService = inject(MessageService);

  loginForm: FormGroup;
  loading = false;
  errorMessage = '';
  private returnUrl = '';

  constructor() {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    AOS.init({ duration: 800, once: true });
    
    // If already logged in, redirect based on role
    if (this.authService.isLoggedIn()) {
      this.authService.redirectBasedOnRole();
      return;
    }

    // Get return URL from query params
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '';
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    const { email, password } = this.loginForm.value;

    this.authService.login(email, password).subscribe({
      next: () => {
        this.loading = false;
        
        // Show success toast
        this.messageService.add({
          severity: 'success',
          summary: 'Connexion réussie',
          detail: 'Bienvenue sur Qartis !',
          life: 3000
        });
        
        // Redirect to return URL or based on role
        setTimeout(() => {
          if (this.returnUrl) {
            this.router.navigateByUrl(this.returnUrl);
          } else {
            this.authService.redirectBasedOnRole();
          }
        }, 500);
      },
      error: (error) => {
        this.loading = false;
        this.handleLoginError(error);
      }
    });
  }

  private handleLoginError(error: any): void {
    const message = error.error?.message;
    
    if (this.isBilingualMessage(message)) {
      // Show bilingual toast
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
      const defaultMsg = 'Email ou mot de passe incorrect';
      this.messageService.add({
        severity: 'error',
        summary: 'Erreur de connexion',
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
  get emailInvalid(): boolean {
    const ctrl = this.loginForm.get('email');
    return !!(ctrl?.invalid && ctrl?.touched);
  }

  get passwordInvalid(): boolean {
    const ctrl = this.loginForm.get('password');
    return !!(ctrl?.invalid && ctrl?.touched);
  }
}
