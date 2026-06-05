# TaskFlow – Gestionnaire de Projets Scrum

> Application fullstack de gestion de projets en mode Scrum avec tableau Kanban.
> Stack : **Spring Boot 3** · **Angular 17** · **MariaDB 11** · **Docker**

---

## Table des matières

1. [Architecture](#architecture)
2. [Prérequis](#prérequis)
3. [Démarrage rapide](#démarrage-rapide-docker)
4. [Structure du projet](#structure-du-projet)
5. [Backend – Spring Boot](#backend--spring-boot)
6. [Frontend – Angular](#frontend--angular)
7. [API REST](#api-rest)
8. [Comptes de test](#comptes-de-test)
9. [CI/CD GitLab](#cicd-gitlab)

---

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│  Browser                                                         │
│  Angular 19  (port 4200)                                          │
│     ├── Login (JWT)                                              │
│     └── Kanban Board (drag & drop)                              │
└───────────────────────┬─────────────────────────────────────────┘
                        │ /api/*  (proxy Nginx)
┌───────────────────────▼─────────────────────────────────────────┐
│  Spring Boot 3  (port 8080)                                      │
│     ├── AuthController  POST /api/auth/login                     │
│     ├── TaskController  CRUD /api/tasks/**                       │
│     └── ProjectController CRUD /api/projects/**                 │
└───────────────────────┬─────────────────────────────────────────┘
                        │ JDBC
┌───────────────────────▼─────────────────────────────────────────┐
│  MariaDB 11  (port 3306)                                         │
│     tables : users · projects · tasks                           │
└─────────────────────────────────────────────────────────────────┘
```

---

## Prérequis

| Outil | Version minimale |
|-------|-----------------|
| Docker + Docker Compose | 24+ |
| Java (dev local) | 17 |
| Maven (dev local) | 3.9 |
| Node.js (dev local) | 20 LTS |

---

## Démarrage rapide (Docker)

```bash
# 1. Cloner le dépôt
git clone <url-du-repo>
cd taskflow

# 2. Lancer toute la stack
docker compose up -d

# 3. Vérifier que les services sont UP
docker compose ps

# 4. Ouvrir l'application
# Frontend  → http://localhost:4200/
# API       → http://localhost:8080/api
# Actuator  → http://localhost:8080/actuator/health
```

### Commandes utiles

```bash
# Logs en temps réel
docker compose logs -f backend

# Relancer seulement le backend après un changement
docker compose up -d --build backend

# Stopper et supprimer les containers
docker compose down

# Stopper ET supprimer les volumes (RESET de la base)
docker compose down -v
```

---

## Structure du projet

```
taskflow/
├── docker-compose.yml          # orchestration de la stack complète
├── init.sql                    # données de démo injectées au 1er démarrage
├── .gitlab-ci.yml              # pipeline CI/CD
│
├── taskflow-backend/           # Spring Boot 3 – API REST
│   ├── Dockerfile.back
│   ├── pom.xml
│   └── src/main/java/ma/rania/taskflow/
│       ├── TaskflowApplication.java
│       ├── config/             # SecurityConfig (Spring Security + CORS)
│       ├── controller/         # AuthController, TaskController, ProjectController
│       ├── dto/                # LoginRequest/Response, TaskDTO, TaskResponse
│       ├── model/              # User, Project, Task (entités JPA)
│       ├── repository/         # UserRepository, ProjectRepository, TaskRepository
│       ├── security/           # JwtUtil, JwtFilter, UserDetailsServiceImpl
│       └── service/            # AuthService, TaskService, ProjectService
│
└── taskflow-frontend/          # Angular 17 – SPA
    ├── Dockerfile.front
    ├── nginx.conf              # SPA routing + proxy /api → backend
    ├── angular.json
    ├── package.json
    └── src/
        ├── app/
        │   ├── app.module.ts
        │   ├── app-routing.module.ts
        │   ├── app.component.ts
        │   ├── core/
        │   │   ├── guards/      auth.guard.ts
        │   │   ├── interceptors/ jwt.interceptor.ts
        │   │   ├── models/      task.model.ts · user.model.ts
        │   │   └── services/    auth.service.ts · task.service.ts
        │   └── features/
        │       ├── auth/login/  login.component.ts + .html
        │       └── tasks/task-board/ task-board.component.ts + .html
        └── environments/
            ├── environment.ts       # dev  → http://localhost:8080/api
            └── environment.prod.ts  # prod → /api (proxy Nginx)
```

---

## Backend – Spring Boot

### Technologies

- **Spring Boot 3.2** + **Spring Security** + **Spring Data JPA**
- **JWT** via JJWT 0.12
- **MariaDB** + Hibernate (ddl-auto = update)
- **Lombok** pour réduire le boilerplate
- **BCrypt** (strength 12) pour les mots de passe

### Développement local (sans Docker)

```bash
cd taskflow-backend

# Pré-requis : MariaDB running sur localhost:3306
# Base : taskflow, user : taskflow / taskflow123

mvn spring-boot:run
```

### Variables d'environnement

| Variable | Défaut |
|----------|--------|
| `SPRING_DATASOURCE_URL` | `jdbc:mariadb://localhost:3306/taskflow` |
| `SPRING_DATASOURCE_USERNAME` | `taskflow` |
| `SPRING_DATASOURCE_PASSWORD` | `taskflow123` |
| `JWT_SECRET` | `taskflow-super-secret-key-…` |
| `JWT_EXPIRATION_MS` | `86400000` (24h) |

---

## Frontend – Angular

### Technologies

- **Angular 17** + **Angular Material** + **Angular CDK**
- **Reactive Forms** + validation côté client
- **JWT Interceptor** (injection automatique du token dans chaque requête)
- **Auth Guard** (protection des routes privées)
- **Drag & Drop** Kanban via `@angular/cdk/drag-drop`

### Développement local (sans Docker)

```bash
cd taskflow-frontend
npm install
npm start          # http://localhost:4200
```

---

## API REST

### Authentification

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `POST` | `/api/auth/login` | Connexion → retourne un JWT |

**Corps de la requête :**
```json
{ "email": "admin@rania.ma", "password": "password123" }
```

**Réponse :**
```json
{
  "token": "eyJhbGciOi...",
  "email": "admin@rania.ma",
  "fullName": "Admin rania",
  "role": "ADMIN"
}
```

### Tâches (`Authorization: Bearer <token>` requis)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/api/tasks/project/{id}` | Tâches d'un projet |
| `GET` | `/api/tasks/project/{id}/status/{status}` | Filtrer par statut |
| `GET` | `/api/tasks/my-tasks/{userId}` | Mes tâches |
| `GET` | `/api/tasks/{id}` | Détail d'une tâche |
| `POST` | `/api/tasks` | Créer une tâche |
| `PUT` | `/api/tasks/{id}` | Mettre à jour |
| `PATCH` | `/api/tasks/{id}/status` | Changer le statut (Kanban) |
| `DELETE` | `/api/tasks/{id}` | Supprimer |

### Projets

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/api/projects` | Liste des projets actifs |
| `GET` | `/api/projects/{id}` | Détail d'un projet |
| `POST` | `/api/projects` | Créer un projet |
| `PUT` | `/api/projects/{id}` | Mettre à jour |
| `DELETE` | `/api/projects/{id}` | Archiver (soft delete) |

---

## Comptes de test

| Email | Mot de passe | Rôle |
|-------|-------------|------|
| `admin@rania.ma` | `password123` | ADMIN |
| `manager@rania.ma` | `password123` | MANAGER |
| `ranya@rania.ma` | `password123` | USER |

---

## CI/CD GitLab

Le fichier `.gitlab-ci.yml` définit un pipeline en 3 stages :

| Stage | Job | Description |
|-------|-----|-------------|
| `test` | `test-backend` | `mvn test` |
| `test` | `test-frontend` | `ng test` (headless Chrome) |
| `build` | `build-backend` | Build Docker + push registry |
| `build` | `build-frontend` | Build Docker + push registry |
| `deploy` | `deploy-staging` | SSH + docker compose pull & up |

**Variables à configurer dans GitLab** (`Settings > CI/CD > Variables`) :

| Variable | Description |
|----------|-------------|
| `CI_REGISTRY_USER` | Utilisateur du registry Docker |
| `CI_REGISTRY_PASSWORD` | Mot de passe du registry |
| `DEPLOY_HOST` | Hôte de déploiement |
| `DEPLOY_USER` | Utilisateur SSH |
| `SSH_PRIVATE_KEY` | Clé privée SSH (masked) |

---

## ScreenShot

![HealthCheck API](https://github.com/raniabounjem-eng/TaskFlow/blob/develop/API_Screen.png)
![Formulaire Login](https://github.com/raniabounjem-eng/TaskFlow/blob/develop/LoginForm_Screen.png)
![KANBAN](https://github.com/raniabounjem-eng/TaskFlow/blob/develop/Kanban_Screen.png)

---

## Licence

Projet pédagogique – Formation 2026.
