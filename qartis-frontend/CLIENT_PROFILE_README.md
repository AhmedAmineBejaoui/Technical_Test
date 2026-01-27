# Qartis Client Profile Page

## Vue d'ensemble

La page Client Profile a été complètement reconstruite avec:
- Chargement du profil utilisateur via GET `/api/client/me`
- Mode édition pour modifier firstName/lastName
- Gestion de la company avec un modal réutilisable
- Animations AOS (Animate On Scroll) sur tous les cards
- Skeletons de chargement pour une meilleure UX
- Support complet du responsive design

## Fonctionnalités

### 1. Affichage du Profil
```
GET /api/client/me
```
- Charge les informations de l'utilisateur connecté
- Affiche: firstName, lastName, email (readonly), role
- Le role s'affiche avec un badge (ADMIN=danger, CLIENT=info)

### 2. Édition du Profil
- Mode édition toggle via bouton crayon
- Formulaire réactif avec validation
- Modification de firstName et lastName
- Email en lecture seule
- Toast de succès après mise à jour
```
PUT /api/client/me
Body: {
  firstName: string,
  lastName: string
}
```

### 3. Gestion de la Company

#### Pas de Company
- Affiche un empty state avec icône
- Bouton "Créer une entreprise" ouvre le modal

#### Avec Company
- Affiche les informations: nom, adresse, TVA, téléphone
- Téléphone est cliquable (tel: link)
- Bouton "Modifier" ouvre le modal en mode édition

#### CompanyModalComponent
Modal réutilisable pour créer/modifier une company:

**Création:**
```
POST /api/client/me/company
Body: {
  name: string (requis),
  address?: string,
  taxNumber?: string,
  phone?: string
}
```

**Modification:**
```
PUT /api/client/me/company
Body: {
  name: string (requis),
  address?: string,
  taxNumber?: string,
  phone?: string
}
```

## Animations AOS

Toutes les pages ont des animations au scroll:

### Login Component
- `data-aos="fade-up"` sur le card principal
- `data-aos="fade-down"` sur le header
- `data-aos="fade-right"` sur le champ email
- `data-aos="fade-left"` sur le champ password

### Signup Component
- Structure identique au login
- Animations staggered sur les champs de formulaire

### Profile Component
- Card principal: `data-aos="fade-up"`
- Card company: `data-aos="fade-up" data-aos-delay="100"`
- Tous les éléments internes animés

### Users Table (Admin)
- Header: `data-aos="fade-down"`
- Toolbar: `data-aos="fade-up"`

## Skeletons & Loading States

### Profile Page
- 3 skeletons pour les champs lors du chargement du profil
- 3 skeletons pour les champs de la company
- Affichage du contenu une fois chargé

### Table Users
- Skeleton pour chaque ligne lors du chargement

### CompanyModal
- Pas de skeleton, mais `[disabled]="submitting"` sur les inputs
- Bouton submit avec `[loading]="submitting"`

## Installation et Build

```bash
# Installer les dépendances (déjà fait)
npm install aos
npm install --save-dev @types/aos

# Build development
npx ng build --configuration=development

# Build production
npx ng build

# Serve
ng serve
```

## Structure des Fichiers

```
src/app/
├── features/
│   ├── client/
│   │   └── profile/
│   │       ├── profile.component.ts (entièrement refactorisé)
│   │       ├── profile.component.html (entièrement refactorisé)
│   │       └── profile.component.css (nouveau avec animations)
│   └── auth/
│       ├── login/
│       │   ├── login.component.ts (ajout AOS)
│       │   └── login.component.html (ajout data-aos)
│       └── signup/
│           ├── signup.component.ts (ajout AOS)
│           └── signup.component.html (ajout data-aos)
├── shared/
│   └── components/
│       └── company-modal/
│           ├── company-modal.component.ts (NOUVEAU)
│           ├── company-modal.component.css (NOUVEAU)
├── services/
│   ├── user.service.ts (endpoint mise à jour: /client/me)
│   └── company.service.ts (endpoints client ajoutés)
└── styles.css (ajout import aos.css)
```

## Points Importants

### Endpoints Backend Utilsés
- `GET /api/client/me` - Charger le profil
- `PUT /api/client/me` - Modifier le profil
- `GET /api/client/me/company` - Charger la company (peut retourner 404)
- `POST /api/client/me/company` - Créer une company
- `PUT /api/client/me/company` - Modifier la company

### Gestion des Erreurs
- Erreur 404 lors du chargement de company = `this.company = null`
- Toast d'erreur bilingue en cas d'échec
- Messages d'erreur API affichés dans le modal

### TypeScript
- Importation de `AOS` depuis le package `aos`
- Utilisation de `AOS.init()` dans chaque composant ngOnInit
- Configuration: `{ duration: 800, once: true }`

### PrimeNG Components Utilisés
- `p-card` - Pour les cartes de profil et company
- `p-dialog` - Pour le modal company
- `p-button` - Pour les boutons
- `p-message` - Pour les messages d'erreur
- `p-skeleton` - Pour les états de chargement
- `p-tag` - Pour les badges de rôle
- `p-toast` - Pour les notifications de succès

## Performance

- **Profile chunk**: 49.32 kB (lazy loaded)
- **CSS total**: 493.04 kB (incluant PrimeNG + AOS)
- **Build time**: ~6 secondes en development
- **Bundle**: 2.28 MB initial (optimisé avec lazy loading)

## SSR Compatibility

Les modifications n'interfèrent pas avec SSR:
- AOS s'initialise côté client uniquement
- Les Skeletons s'affichent correctement
- Les routes dynamiques sont restées stables
- Pas de code côté serveur ajouté

## Test

Pour tester la fonctionnalité:

1. Lancez le backend Spring Boot sur le port 8080
2. Connectez-vous avec un compte CLIENT
3. Accédez à `/client/profile`
4. Testez l'édition du profil
5. Créez/modifiez une company via le modal
6. Vérifiez les animations AOS au scroll

## Améliorations Futures

- [ ] Photo de profil utilisateur
- [ ] Historique des modifications
- [ ] Export des données en PDF
- [ ] Partage de company avec d'autres users
- [ ] Notifications en temps réel
