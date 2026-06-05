package ma.rania.taskflow.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.rania.taskflow.model.Project;
import ma.rania.taskflow.model.Task;
import ma.rania.taskflow.model.User;
import ma.rania.taskflow.repository.ProjectRepository;
import ma.rania.taskflow.repository.TaskRepository;
import ma.rania.taskflow.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Insère les données de démonstration au démarrage si la base est vide.
 * Utilise le PasswordEncoder Spring → hash BCrypt garanti correct.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository    userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository    taskRepository;
    private final PasswordEncoder   passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        // Toujours s'assurer que les comptes de démo existent avec le bon mot de passe
        upsertUser("Admin rania",   "admin@rania.ma",   User.Role.ADMIN);
        upsertUser("Manager Test",  "manager@rania.ma", User.Role.MANAGER);
        upsertUser("Ranya Bounjem", "ranya@rania.ma",   User.Role.USER);

        if (projectRepository.count() == 0) {
            seedProjects();
        }
        log.info("DataInitializer : OK ({} users, {} projets, {} tâches)",
                userRepository.count(), projectRepository.count(), taskRepository.count());
    }

    private void upsertUser(String fullName, String email, User.Role role) {
        String hash = passwordEncoder.encode("password123");
        userRepository.findByEmail(email).ifPresentOrElse(
            u -> {
                // Met à jour le hash pour corriger d'éventuels hash invalides
                u.setPassword(hash);
                userRepository.save(u);
                log.info("DataInitializer : mot de passe mis à jour pour {}", email);
            },
            () -> {
                userRepository.save(User.builder()
                        .fullName(fullName).email(email)
                        .password(hash).role(role).build());
                log.info("DataInitializer : utilisateur créé : {}", email);
            }
        );
    }

    private void seedProjects() {
        User admin   = userRepository.findByEmail("admin@rania.ma").orElseThrow();
        User manager = userRepository.findByEmail("manager@rania.ma").orElseThrow();

        Project sprint1 = projectRepository.save(Project.builder()
                .name("TaskFlow Sprint 1")
                .description("Premier sprint de développement de TaskFlow")
                .startDate(LocalDate.now()).active(true).build());

        projectRepository.save(Project.builder()
                .name("Refonte Portail Client")
                .description("Modernisation du portail client Orange Maroc")
                .startDate(LocalDate.now()).active(true).build());

        taskRepository.save(task("Configurer MariaDB",       "Installer et configurer la base de données",       Task.Status.DONE,        Task.Priority.HIGH,     sprint1, admin));
        taskRepository.save(task("Créer entités JPA",        "User, Project, Task avec Hibernate",               Task.Status.DONE,        Task.Priority.HIGH,     sprint1, admin));
        taskRepository.save(task("Développer API REST",      "Controllers Spring Boot pour les 3 entités",       Task.Status.IN_PROGRESS, Task.Priority.HIGH,     sprint1, manager));
        taskRepository.save(task("Sécurité JWT",             "Implémenter Spring Security + JWT",                Task.Status.IN_PROGRESS, Task.Priority.CRITICAL, sprint1, manager));
        taskRepository.save(task("Frontend Angular – Login", "Formulaire de connexion avec Angular Material",    Task.Status.REVIEW,      Task.Priority.MEDIUM,   sprint1, null));
        taskRepository.save(task("Tableau Kanban",           "Composant drag & drop avec Angular CDK",           Task.Status.REVIEW,      Task.Priority.MEDIUM,   sprint1, null));
        taskRepository.save(task("Dockeriser l'application", "Dockerfile + docker-compose pour tout le stack",   Task.Status.TODO,        Task.Priority.LOW,      sprint1, null));
        taskRepository.save(task("Pipeline GitLab CI/CD",   "Configurer le .gitlab-ci.yml",                     Task.Status.TODO,        Task.Priority.LOW,      sprint1, null));
    }

    private Task task(String title, String desc, Task.Status status, Task.Priority priority,
                      Project project, User assignee) {
        return Task.builder()
                .title(title).description(desc)
                .status(status).priority(priority)
                .project(project).assignee(assignee)
                .build();
    }
}
