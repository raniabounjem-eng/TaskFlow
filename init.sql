-- init.sql : schéma + données de démarrage pour le développement
-- Exécuté automatiquement par MariaDB au PREMIER démarrage du container
-- (avant que Spring Boot démarre – on crée donc le schéma ici)

USE taskflow;

-- ── Schéma ────────────────────────────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS users (
  id        BIGINT       NOT NULL AUTO_INCREMENT,
  full_name VARCHAR(255) NOT NULL,
  email     VARCHAR(255) NOT NULL UNIQUE,
  password  VARCHAR(255) NOT NULL,
  role      VARCHAR(20)  NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS projects (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  name        VARCHAR(255) NOT NULL,
  description TEXT,
  start_date  DATE,
  active      BOOLEAN      NOT NULL DEFAULT TRUE,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tasks (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  title       VARCHAR(255) NOT NULL,
  description TEXT,
  status      VARCHAR(20)  NOT NULL DEFAULT 'TODO',
  priority    VARCHAR(20)  NOT NULL DEFAULT 'MEDIUM',
  due_date    DATE,
  project_id  BIGINT       NOT NULL,
  assignee_id BIGINT,
  created_at  DATETIME,
  updated_at  DATETIME,
  PRIMARY KEY (id),
  CONSTRAINT fk_task_project  FOREIGN KEY (project_id)  REFERENCES projects(id),
  CONSTRAINT fk_task_assignee FOREIGN KEY (assignee_id) REFERENCES users(id)
);

-- ── Données de test ───────────────────────────────────────────────────────────
-- Mots de passe hashés BCrypt – valeur réelle : "password123"

INSERT IGNORE INTO users (full_name, email, password, role) VALUES
  ('Admin rania',   'admin@rania.ma',   '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj/o4YxI9eJq', 'ADMIN'),
  ('Manager Test',  'manager@rania.ma', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj/o4YxI9eJq', 'MANAGER'),
  ('Ranya Bounjem', 'ranya@rania.ma',   '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj/o4YxI9eJq', 'USER');

INSERT IGNORE INTO projects (name, description, start_date, active) VALUES
  ('TaskFlow Sprint 1',      'Premier sprint de développement de TaskFlow',     CURDATE(), TRUE),
  ('Refonte Portail Client', 'Modernisation du portail client Orange Maroc',    CURDATE(), TRUE);

INSERT IGNORE INTO tasks (title, description, status, priority, project_id, created_at, updated_at) VALUES
  ('Configurer MariaDB',        'Installer et configurer la base de données',        'DONE',        'HIGH',     1, NOW(), NOW()),
  ('Créer entités JPA',         'User, Project, Task avec Hibernate',                'DONE',        'HIGH',     1, NOW(), NOW()),
  ('Développer API REST',       'Controllers Spring Boot pour les 3 entités',        'IN_PROGRESS', 'HIGH',     1, NOW(), NOW()),
  ('Sécurité JWT',              'Implémenter Spring Security + JWT',                 'IN_PROGRESS', 'CRITICAL', 1, NOW(), NOW()),
  ('Frontend Angular – Login',  'Formulaire de connexion avec Angular Material',     'REVIEW',      'MEDIUM',   1, NOW(), NOW()),
  ('Tableau Kanban',            'Composant drag & drop avec Angular CDK',            'REVIEW',      'MEDIUM',   1, NOW(), NOW()),
  ('Dockeriser l''application', 'Dockerfile + docker-compose pour tout le stack',    'TODO',        'LOW',      1, NOW(), NOW()),
  ('Pipeline GitLab CI/CD',     'Configurer le .gitlab-ci.yml',                      'TODO',        'LOW',      1, NOW(), NOW());


-- Projets de démonstration
INSERT IGNORE INTO projects (name, description, start_date, active) VALUES
  ('TaskFlow Sprint 1',     'Premier sprint de développement de TaskFlow',      CURDATE(), true),
  ('Refonte Portail Client','Modernisation du portail client Orange Maroc',      CURDATE(), true);

-- Tâches de démonstration (project_id = 1 = TaskFlow Sprint 1)
INSERT IGNORE INTO tasks (title, description, status, priority, project_id, created_at, updated_at) VALUES
  ('Configurer MariaDB',        'Installer et configurer la base de données',          'DONE',        'HIGH',     1, NOW(), NOW()),
  ('Créer entités JPA',         'User, Project, Task avec Hibernate',                  'DONE',        'HIGH',     1, NOW(), NOW()),
  ('Développer API REST',       'Controllers Spring Boot pour les 3 entités',          'IN_PROGRESS', 'HIGH',     1, NOW(), NOW()),
  ('Sécurité JWT',              'Implémenter Spring Security + JWT',                   'IN_PROGRESS', 'CRITICAL', 1, NOW(), NOW()),
  ('Frontend Angular – Login',  'Formulaire de connexion avec Angular Material',       'REVIEW',      'MEDIUM',   1, NOW(), NOW()),
  ('Tableau Kanban',            'Composant drag & drop avec Angular CDK',              'REVIEW',      'MEDIUM',   1, NOW(), NOW()),
  ('Dockeriser l''application', 'Dockerfile + docker-compose pour tout le stack',      'TODO',        'LOW',      1, NOW(), NOW()),
  ('Pipeline GitLab CI/CD',     'Configurer le .gitlab-ci.yml',                        'TODO',        'LOW',      1, NOW(), NOW());
