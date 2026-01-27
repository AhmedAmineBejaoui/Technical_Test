import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { SkeletonModule } from 'primeng/skeleton';
import { MessageService } from 'primeng/api';

import { CompanyService } from '../../../services/company.service';
import { Company } from '../../../models/user.model';
import { BilingualMessage } from '../../../models/auth.model';

@Component({
  selector: 'app-company-detail',
  standalone: true,
  imports: [
    CommonModule,
    CardModule,
    ButtonModule,
    TagModule,
    SkeletonModule
  ],
  templateUrl: './company-detail.component.html',
  styleUrls: ['./company-detail.component.css']
})
export class CompanyDetailComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly companyService = inject(CompanyService);
  private readonly messageService = inject(MessageService);

  company: Company | null = null;
  loading = true;
  error = '';

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadCompany(+id);
    } else {
      this.goBack();
    }
  }

  loadCompany(id: number): void {
    this.loading = true;
    this.error = '';

    this.companyService.getCompanyById(id).subscribe({
      next: (company: Company) => {
        this.company = company;
        this.loading = false;
      },
      error: (err: any) => {
        this.loading = false;
        this.showErrorToast(err);
        this.error = 'Entreprise non trouvée';
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/admin/users']);
  }

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
        detail: 'Entreprise non trouvée',
        life: 5000
      });
    }
  }

  private isBilingualMessage(obj: any): obj is BilingualMessage {
    return obj && typeof obj === 'object' && 'en' in obj && 'ar' in obj;
  }
}
