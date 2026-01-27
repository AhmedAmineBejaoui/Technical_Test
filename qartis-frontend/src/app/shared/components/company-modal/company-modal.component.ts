import { Component, Input, Output, EventEmitter, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { SkeletonModule } from 'primeng/skeleton';
import { CompanyService } from '../../../services/company.service';
import { Company } from '../../../models/user.model';

@Component({
  selector: 'app-company-modal',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    DialogModule,
    InputTextModule,
    ButtonModule,
    MessageModule,
    SkeletonModule
  ],
  template: `
    <p-dialog 
      [visible]="visible" 
      [modal]="true" 
      [header]="isEdit ? 'Modifier Entreprise' : 'Créer Entreprise'"
      [closable]="!submitting"
      (onHide)="onHide()">
      
      <form [formGroup]="companyForm" (ngSubmit)="onSubmit()">
        <p-message 
          *ngIf="errorMessage" 
          severity="error" 
          [text]="errorMessage"
          styleClass="w-full mb-3">
        </p-message>

        <div class="field mb-4">
          <label for="name" class="block mb-2 font-semibold">
            <i class="pi pi-building mr-2"></i>Nom de l'entreprise
          </label>
          <input 
            pInputText 
            id="name" 
            formControlName="name"
            class="w-full"
            [disabled]="submitting"
            placeholder="Ex: Acme Corp" />
          <small class="text-red-500" *ngIf="companyForm.get('name')?.invalid && companyForm.get('name')?.touched">
            Le nom est requis
          </small>
        </div>

        <div class="field mb-4">
          <label for="address" class="block mb-2 font-semibold">
            <i class="pi pi-map-marker mr-2"></i>Adresse
          </label>
          <input 
            pInputText 
            id="address" 
            formControlName="address"
            class="w-full"
            [disabled]="submitting"
            placeholder="Ex: 123 Rue du Commerce, 75001 Paris" 
            style="min-height: 100px; padding: 12px; font-family: inherit; resize: vertical;" />
        </div>

        <div class="field mb-4">
          <label for="taxNumber" class="block mb-2 font-semibold">
            <i class="pi pi-file-pdf mr-2"></i>Numéro de TVA
          </label>
          <input 
            pInputText 
            id="taxNumber" 
            formControlName="taxNumber"
            class="w-full"
            [disabled]="submitting"
            placeholder="Ex: FR12345678901" />
        </div>

        <div class="field mb-4">
          <label for="phone" class="block mb-2 font-semibold">
            <i class="pi pi-phone mr-2"></i>Téléphone
          </label>
          <input 
            pInputText 
            id="phone" 
            formControlName="phone"
            class="w-full"
            [disabled]="submitting"
            placeholder="Ex: +33 1 23 45 67 89" />
        </div>

        <div class="flex gap-2 justify-content-end">
          <p-button 
            label="Annuler" 
            severity="secondary"
            [disabled]="submitting"
            (onClick)="onHide()">
          </p-button>
          <p-button 
            [label]="isEdit ? 'Modifier' : 'Créer'"
            icon="pi pi-save"
            [loading]="submitting"
            [disabled]="companyForm.invalid"
            type="submit">
          </p-button>
        </div>
      </form>
    </p-dialog>
  `,
  styleUrls: ['./company-modal.component.css']
})
export class CompanyModalComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly companyService = inject(CompanyService);

  @Input() visible = false;
  @Input() company: Company | null = null;
  @Input() isEdit = false;
  @Output() visibleChange = new EventEmitter<boolean>();
  @Output() success = new EventEmitter<Company>();

  companyForm: FormGroup;
  submitting = false;
  errorMessage = '';

  constructor() {
    this.companyForm = this.fb.group({
      name: ['', Validators.required],
      address: [''],
      taxNumber: [''],
      phone: ['']
    });
  }

  ngOnInit(): void {
    if (this.isEdit && this.company) {
      this.companyForm.patchValue({
        name: this.company.name,
        address: this.company.address || '',
        taxNumber: this.company.taxNumber || '',
        phone: this.company.phone || ''
      });
    }
  }

  onSubmit(): void {
    if (!this.companyForm.valid) return;

    this.submitting = true;
    this.errorMessage = '';
    const formValue = this.companyForm.value;

    const request = this.isEdit
      ? this.companyService.updateCompany(formValue)
      : this.companyService.createCompany(formValue);

    request.subscribe({
      next: (company) => {
        this.submitting = false;
        this.success.emit(company);
        this.onHide();
      },
      error: (error) => {
        this.submitting = false;
        this.errorMessage = error.error?.message || 'Erreur lors de l\'opération';
      }
    });
  }

  onHide(): void {
    if (!this.submitting) {
      this.visible = false;
      this.visibleChange.emit(false);
      this.companyForm.reset();
      this.errorMessage = '';
    }
  }
}
