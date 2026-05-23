# 🏢 Qartis Backend API

API REST pour la gestion des utilisateurs, entreprises et authentification JWT.

## 📋 Description

Le backend Qartis est une API REST complète construite avec **Spring Boot 3.2.1**, offrant:
- ✅ Authentification JWT avec Spring Security
- ✅ Gestion des utilisateurs (Admin & Client)
- ✅ Gestion des entreprises (Company)
- ✅ Contrôle d'accès basé sur les rôles (RBAC)
- ✅ Documentation OpenAPI/Swagger intégrée
- ✅ Mapper DTO avec MapStruct
- ✅ Pagination et filtrage côté serveur
- ✅ Gestion centralisée des exceptions

## 🛠 Tech Stack

| Technologie | Version | Usage |
|-------------|---------|-------|
| **Spring Boot** | 3.2.1 | Framework web |
| **Spring Security** | 6.x | Authentification & Autorisation |
| **Spring Data JPA** | 3.2.1 | Persistance & ORM |
| **MySQL** | 8.0+ | Base de données |
| **JWT (jjwt)** | 0.12.3 | Token JWT |
| **MapStruct** | 1.5.5 | Mapping DTO ↔ Entity |
| **Springdoc OpenAPI** | 2.2.0 | Documentation Swagger |
| **Lombok** | 1.18.30 | Réduction de boilerplate |

## 📦 Prérequis

- **JDK 17+** (Java 17 ou supérieur)
- **Maven 3.8+**
- **MySQL 8.0+**

### Vérifier les installations

```bash
# Vérifier Java
java -version

# Vérifier Maven
mvn -version

# Vérifier MySQL
mysql --version
```

## 🗄️ Configuration MySQL

### 1. Créer la base de données

```bash
# Connectez-vous à MySQL
mysql -u root -p

# Exécutez les commandes SQL suivantes
CREATE DATABASE qartis CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'qartis_user'@'localhost' IDENTIFIED BY 'QartisPassword123!';
GRANT ALL PRIVILEGES ON qartis.* TO 'qartis_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### 2. Configurer application.yml

Modifiez `src/main/resources/application.yml`:

```yaml
spring:
  application:
    name: qartis-app
  datasource:
    url: jdbc:mysql://localhost:3306/qartis?serverTimezone=UTC&useSSL=false
    username: qartis_user
    password: QartisPassword123!
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop  # 'update' en production
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true

server:
  port: 8080

jwt:
<!-- Devora: Remove the secret from code and rotate the credential using vault/secrets manager. -->
  <!-- Devora removed a suspected hardcoded secret. Move the value to a vault or CI/CD secret. -->
  expiration: 86400000
```

## 🚀 Commandes d'exécution

### Compiler

```bash
# Compiler sans tests
mvn clean compile

# Compiler avec tests
mvn clean package
```

### Démarrer l'application

```bash
# Profil dev (par défaut)
mvn spring-boot:run

# Profil spécifique
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Depuis un JAR
java -jar target/qartis-app-1.0.0.jar
```

### Tester

```bash
mvn test
```

## 📖 Documentation API

### Swagger UI

Une fois l'application démarrée:

```
http://localhost:8080/swagger-ui.html
```

### API Docs (JSON)

```
http://localhost:8080/api-docs
```

### Configuration Swagger
- ✅ Bearer Token authentication
- ✅ Tous les endpoints documentés
- ✅ Try it out activé

## 🔐 Credentials Admin Seed

L'application crée automatiquement un admin au démarrage:

```
Email:      admin@demo.com
Mot de passe: Admin@12345
Rôle:       ADMIN
```

## 📡 Endpoints Principaux

### Auth (Public)
```
POST   /auth/login              - Connexion
POST   /auth/signup             - Inscription (CLIENT)
```

### Admin Users
```
GET    /admin/users             - Liste (pagination/filtres)
GET    /admin/users/{id}        - Détail
POST   /admin/users             - Créer
PUT    /admin/users/{id}        - Modifier
DELETE /admin/users/{id}        - Supprimer
PATCH  /admin/users/{id}/toggle-status - Activer/Désactiver
```

### Admin Companies
```
GET    /admin/companies         - Liste
GET    /admin/companies/{id}    - Détail
```

### Client Profile
```
GET    /client/me               - Mon profil
PUT    /client/me               - Modifier profil
GET    /client/me/company       - Mon entreprise
POST   /client/me/company       - Créer entreprise
PUT    /client/me/company       - Modifier entreprise
```

## 🔑 Exemple Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@demo.com",
    "password": "Admin@12345"
  }'
```

Réponse:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": 1,
    "firstName": "Admin",
    "lastName": "User",
    "email": "admin@demo.com",
    "role": "ADMIN"
  }
}
```

## 🏗️ Architecture

```
src/main/java/com/qartis/app/
├── controller/          # Endpoints
│   ├── AuthController.java
│   ├── AdminUserController.java
│   ├── AdminCompanyController.java
│   └── ClientController.java
├── service/             # Business logic
├── repository/          # Data access
├── dto/                 # Request/Response
├── entity/              # JPA entities
├── mapper/              # Entity ↔ DTO
├── security/            # JWT & Security
├── exception/           # Error handling
└── config/              # Spring config
```

## 🐛 Dépannage

### MySQL ne démarre pas
```bash
# Windows
net start MySQL80

# Linux
sudo service mysql start

# macOS
brew services start mysql-server
```

### Port 8080 déjà utilisé
```yaml
server:
  port: 8081
```

### Erreur JWT
- Vérifiez le format: `Authorization: Bearer <token>`
- Vérifiez que le token n'a pas expiré (24h)

## ✅ Quick Start Checklist

- [ ] JDK 17+ installé
- [ ] Maven installé
- [ ] MySQL démarré
- [ ] Base `qartis` créée
- [ ] Utilisateur `qartis_user` créé
- [ ] `application.yml` configuré
- [ ] `mvn spring-boot:run` exécuté
- [ ] Swagger accessible
- [ ] Login réussi avec admin@demo.com

---

**Version**: 1.0.0 | **Janvier 2026**
