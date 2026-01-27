# Qartis Project

## Overview
The Qartis project consists of two main components:

1. **Qartis Backend**: A Spring Boot application that provides the backend services for the project.
2. **Qartis Frontend**: An Angular application that serves as the user interface for the project.

---

## Project Structure

### Backend
- **Path**: `qartis-backend/`
- **Description**: Contains the Spring Boot application.
- **Key Files and Directories**:
  - `src/main/java/com/qartis/app/`: Contains the main application code.
  - `src/main/resources/`: Contains configuration files like `application.yml`.
  - `pom.xml`: Maven configuration file.

### Frontend
- **Path**: `qartis-frontend/`
- **Description**: Contains the Angular application.
- **Key Files and Directories**:
  - `src/`: Contains the main application code.
  - `angular.json`: Angular CLI configuration file.
  - `package.json`: Node.js dependencies and scripts.

---

## How to Run

### Backend
1. Navigate to the backend directory:
   ```bash
   cd qartis-backend
   ```
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### Frontend
1. Navigate to the frontend directory:
   ```bash
   cd qartis-frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Run the application:
   ```bash
   ng serve
   ```

---

## Configuration

### Backend
- **Database Configuration**: Update the `application-dev.yml` file in `src/main/resources/` with your database credentials.
- **Profiles**: The backend supports multiple profiles (e.g., `dev`, `prod`).

### Frontend
- **Environment Configuration**: Update the `environment.ts` file in `src/environments/` with your API endpoints.

---

## Testing

### Backend
- Run tests using Maven:
  ```bash
  mvn test
  ```

### Frontend
- Run tests using Angular CLI:
  ```bash
  ng test
  ```

---

## Contribution
1. Fork the repository.
2. Create a new branch for your feature:
   ```bash
   git checkout -b feature-name
   ```
3. Commit your changes:
   ```bash
   git commit -m "Description of changes"
   ```
4. Push to your branch:
   ```bash
   git push origin feature-name
   ```
5. Create a pull request.

---

## License
This project is licensed under the MIT License.