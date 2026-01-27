# 🚀 Qartis Frontend

Application Angular moderne pour la gestion des utilisateurs avec interface admin et client.

## 📋 Description

Le frontend Qartis est une application **Angular 21** complète avec:
- ✅ Authentification JWT avec guards basés sur les rôles
- ✅ Interface Admin: Gestion des utilisateurs avec lazy loading
- ✅ Interface Client: Profil utilisateur et gestion d'entreprise
- ✅ Composants PrimeNG v21 modernes et responsifs
- ✅ Animations AOS au scroll
- ✅ Bilingual support (Français/English/العربية)
- ✅ Loading skeletons et spinners
- ✅ Toast notifications
- ✅ Formulaires réactifs avec validation

## 🛠 Tech Stack

| Technologie | Version | Usage |
|-------------|---------|-------|
| **Angular** | 21 | Framework SPA |
| **Angular Standalone** | 21 | Composants modernes |
| **PrimeNG** | 21 | Composants UI |
| **PrimeFlex** | 3.x | Layout CSS |
| **AOS** | 2.x | Animations au scroll |
| **TypeScript** | 5.x | Langage |
| **RxJS** | 7.x | Programmation réactive |
| **Node.js** | 18+ / npm | Package manager |

## 📦 Prérequis

- **Node.js 18+**
- **npm 9+** (ou yarn)
- **Angular CLI 21**
- **Backend Qartis** démarré sur `http://localhost:8080`

### Vérifier les installations

```bash
# Vérifier Node.js
node --version

# Vérifier npm
npm --version

# Vérifier Angular CLI
ng version
```

### Installer Angular CLI

```bash
npm install -g @angular/cli@21
```

## 🔧 Configuration API

Modifiez l'URL de l'API dans les services si nécessaire:

**Fichier**: `src/app/services/user.service.ts`
```typescript
private readonly API_URL = 'http://localhost:8080/api';
```

**Fichier**: `src/app/services/company.service.ts`
```typescript
private readonly API_URL = 'http://localhost:8080/api';
```

**Fichier**: `src/app/services/auth.service.ts`
```typescript
private readonly API_URL = 'http://localhost:8080/api';
```

> **Note**: Pour la production, créez des fichiers `environment.prod.ts`

## 🚀 Commandes de lancement

### Installation des dépendances

```bash
cd qartis-frontend

# Installer les packages
npm install

# Ou avec yarn
yarn install
```

### Développement

```bash
# Démarrer le serveur dev (http://localhost:4200)
ng serve

# Ou
npm start

# Avec rechargement auto
ng serve --open
```

### Build

```bash
# Build développement
ng build --configuration=development

# Build production
ng build --configuration=production

# Voir la taille des bundles
ng build --stats-json
```

### Tests

```bash
# Exécuter les tests unitaires
ng test

# Tests avec coverage
ng test --code-coverage
```

## 📄 Pages & Routes

### Routes publiques

```
/login          - Connexion
/signup         - Inscription
```

### Routes Admin (Rôle: ADMIN)

```
/admin/users           - Liste des utilisateurs avec lazy loading
/admin/companies/:id   - Détail d'une entreprise
```

### Routes Client (Rôle: CLIENT)

```
/profile       - Mon profil + gestion d'entreprise
/dashboard     - Tableau de bord (optionnel)
```

### Routes protégées

Toutes les routes, sauf `/login` et `/signup`, nécessitent un token JWT valide.

## 🔐 Authentification & Demo Login

### Credentials de Demo

#### Admin
```
Email:       admin@demo.com
Mot de passe: Admin@12345
Rôle:        ADMIN
Accès:       Gestion des utilisateurs, entreprises
```

#### Client (Inscription)
```
Prénom:      John
Nom:         Doe
Email:       john@example.com
Mot de passe: SecurePass123!
Rôle:        CLIENT
Accès:       Profil personnel, gestion d'entreprise
```

### Flow Authentification

1. **Login** → `/login` → POST `/auth/login`
2. **Token** → Stocké dans `localStorage` sous `auth_token`
3. **Guard** → Redirects automatique selon le rôle
4. **Logout** → Token supprimé du localStorage

### Guards Disponibles

- `authGuard` - Vérifie si l'utilisateur est authentifié
- `adminGuard` - Vérifie que le rôle est ADMIN
- `clientGuard` - Vérifie que le rôle est CLIENT

## 🎨 Pages & Fonctionnalités

### Login Page
- **Form**: Email + Password
- **Validation**: Frontend (email format, required fields)
- **Bilingual Errors**: Messages en français/arabe
- **AOS Animations**: Fade-up sur la card
- **Auto-Redirect**: Selon le rôle (admin → /admin/users, client → /profile)
- **Demo**: `admin@demo.com` / `Admin@12345`

### Signup Page
- **Form**: FirstName, LastName, Email, Password
- **Validation**: 
  - Password strength (uppercase, lowercase, numbers)
  - Email format
  - Min length validation
- **Success**: Redirect vers login
- **AOS Animations**: Fade-up sur la card

### Admin Users Page
- **PrimeNG Table** avec lazy loading
- **Filters**: Email, Name, Company, Role
- **Pagination**: Server-side (page/size)
- **Columns**: ID, Full Name (avatar), Email, Role (badge), Company (clickable), Status, Actions
- **CRUD Operations**:
  - Create: Dialog avec formulaire
  - Edit: Dialog avec pré-remplissage
  - Delete: Confirmation dialog
  - Toggle Status: Activer/Désactiver
- **Loading Skeleton**: Pendant le chargement
- **Toast Notifications**: Success/Error en français/arabe

### Client Profile Page
- **User Info Card**:
  - Display: FirstName, LastName, Email, Role
  - Edit mode: Formulaire réactif modifiable
  - Save button: Avec loading state
  - AOS animation: Fade-up
- **Company Card**:
  - Si pas de company: Bouton "Créer une entreprise"
  - Si company existe:
    - Affiche: Name, Address, TaxNumber, Phone, CreatedAt
    - Bouton "Modifier"
  - AOS animation: Fade-up avec delay
- **Company Modal Dialog**:
  - Formulaire: Name*, Address, TaxNumber, Phone
  - POST pour créer / PUT pour modifier
  - Validation complète
  - Success toast après sauvegarde

## 📊 Composants Principaux

### Layouts
- **AppComponent** - Root component
- **Navigation & Header** - Accessible depuis toutes les pages

### Composants d'Auth
- **LoginComponent** - Connexion utilisateur
- **SignupComponent** - Inscription nouveau client

### Composants Admin
- **UsersComponent** - List avec lazy loading, filtres, CRUD
- **CompanyDetailComponent** - Détail d'une entreprise

### Composants Client
- **ProfileComponent** - Profil utilisateur + company management
- **CompanyModalComponent** - Modal réutilisable pour créer/modifier company

## 🎭 Bilingual Support

Messages en français par défaut. Support arabe pour les erreurs:

```typescript
const message: BilingualMessage = {
  en: 'Error message in English',
  fr: 'Message d\'erreur en français',
  ar: 'رسالة الخطأ بالعربية'
};
```

## 🎬 Animations AOS

- **Library**: Animate On Scroll (AOS)
- **Config**: `duration: 800ms, once: true`
- **Utilisation**: 
  - Cards: `data-aos="fade-up"`
  - Stagger: `data-aos-delay="100"`, `"200"`, etc.
- **Pages affectées**: Login, Signup, Profile, Users Table

## 📦 Structure du Projet

```
src/app/
├── core/
│   ├── auth/
│   │   └── auth.service.ts
│   ├── guards/
│   │   ├── auth.guard.ts
│   │   ├── admin.guard.ts
│   │   └── client.guard.ts
│   └── interceptors/
│       └── http-error.interceptor.ts
├── features/
│   ├── auth/
│   │   ├── login/
│   │   └── signup/
│   ├── admin/
│   │   ├── users/
│   │   └── companies/
│   └── client/
│       └── profile/
├── models/
│   ├── auth.model.ts
│   └── user.model.ts
├── services/
│   ├── user.service.ts
│   ├── company.service.ts
│   └── auth.service.ts
├── shared/
│   ├── components/
│   │   └── company-modal/
│   └── pipes/
├── app.routes.ts
├── app.config.ts
└── app.component.ts
```

## 🚀 Démarrage Rapide

### 1. Installer les dépendances
```bash
npm install
```

### 2. Démarrer le backend
```bash
# Dans le dossier qartis-backend
mvn spring-boot:run
```

### 3. Démarrer le frontend
```bash
ng serve --open
```

### 4. Accéder à l'application
```
http://localhost:4200
```

### 5. Se connecter avec Admin
```
Email:    admin@demo.com
Password: Admin@12345
```

### 6. Navigation
- **Admin**: Allez à `/admin/users` pour voir la liste
- **Client**: Créez un compte via `/signup`, puis allez à `/profile`

## 🔗 Intégration API

### Format des requêtes
```typescript
// Avec token automatique via interceptor
this.http.get('/api/admin/users')
  // Header Authorization: Bearer <token> ajouté auto
```

### Format des réponses Pagination
```json
{
  "content": [...],
  "page": 0,
  "size": 10,
  "totalElements": 50,
  "totalPages": 5,
  "first": true,
  "last": false
}
```

## 🐛 Dépannage

### Erreur CORS
- Vérifiez que le backend est sur `http://localhost:8080`
- Vérifiez la configuration CORS dans Spring Security

### Token expiré
- Rafraîchissez la page ou connectez-vous à nouveau
- Token valide 24h

### Erreur 401 (Unauthorized)
- Vérifiez le token dans DevTools > Application > localStorage > `auth_token`
- Vérifiez que le token est envoyé dans le header Authorization
- Reconnectez-vous

### Port 4200 déjà utilisé
```bash
ng serve --port 4201
```

### Erreur "Cannot find module"
```bash
# Supprimer node_modules et réinstaller
rm -rf node_modules package-lock.json
npm install
```

## ✅ Quick Start Checklist

- [ ] Node.js 18+ installé
- [ ] npm installé (`npm --version`)
- [ ] Angular CLI installé (`ng version`)
- [ ] Backend démarré sur localhost:8080
- [ ] `npm install` exécuté
- [ ] `ng serve` lancé
- [ ] Application accessible sur localhost:4200
- [ ] Login réussi avec admin@demo.com
- [ ] Able to navigate to Admin Users page

## 📚 Ressources

- [Angular Documentation](https://angular.io/docs)
- [PrimeNG Components](https://primeng.org/)
- [RxJS Documentation](https://rxjs.dev/)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [AOS Library](https://michalsnik.github.io/aos/)

---

**Version**: 1.0.0 | **Janvier 2026**

