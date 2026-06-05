package ma.rania.taskflow.service;

import lombok.RequiredArgsConstructor;
import ma.rania.taskflow.model.Project;
import ma.rania.taskflow.repository.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public List<Project> getAll() {
        return projectRepository.findByActiveTrue();
    }

    @Transactional(readOnly = true)
    public Project getById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projet introuvable : " + id));
    }

    public Project create(Project project) {
        return projectRepository.save(project);
    }

    public Project update(Long id, Project updated) {
        Project project = getById(id);
        project.setName(updated.getName());
        project.setDescription(updated.getDescription());
        project.setStartDate(updated.getStartDate());
        project.setActive(updated.isActive());
        return projectRepository.save(project);
    }

    public void delete(Long id) {
        Project project = getById(id);
        project.setActive(false);           // soft delete
        projectRepository.save(project);
    }
}
