package ma.rania.taskflow.service;

import lombok.RequiredArgsConstructor;
import ma.rania.taskflow.dto.TaskDTO;
import ma.rania.taskflow.dto.TaskResponse;
import ma.rania.taskflow.model.Project;
import ma.rania.taskflow.model.Task;
import ma.rania.taskflow.model.User;
import ma.rania.taskflow.repository.ProjectRepository;
import ma.rania.taskflow.repository.TaskRepository;
import ma.rania.taskflow.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository    taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository    userRepository;

    @Transactional(readOnly = true)
    public List<TaskResponse> getByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId)
                .stream().map(TaskResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getByProjectAndStatus(Long projectId, Task.Status status) {
        return taskRepository.findByProjectIdAndStatus(projectId, status)
                .stream().map(TaskResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getMyTasks(Long userId) {
        return taskRepository.findByAssigneeId(userId)
                .stream().map(TaskResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getById(Long id) {
        return TaskResponse.from(findTask(id));
    }

    public TaskResponse create(TaskDTO dto) {
        Task task = new Task();
        applyDto(dto, task);
        return TaskResponse.from(taskRepository.save(task));
    }

    public TaskResponse update(Long id, TaskDTO dto) {
        Task task = findTask(id);
        applyDto(dto, task);
        return TaskResponse.from(taskRepository.save(task));
    }

    public TaskResponse updateStatus(Long id, Map<String, String> body) {
        Task task = findTask(id);
        String status = body.get("status");
        if (status == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Champ 'status' manquant");
        task.setStatus(Task.Status.valueOf(status));
        return TaskResponse.from(taskRepository.save(task));
    }

    public void delete(Long id) {
        taskRepository.delete(findTask(id));
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private Task findTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tâche introuvable : " + id));
    }

    private void applyDto(TaskDTO dto, Task task) {
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projet introuvable : " + dto.getProjectId()));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        task.setDueDate(dto.getDueDate());
        task.setProject(project);

        if (dto.getAssigneeId() != null) {
            User assignee = userRepository.findById(dto.getAssigneeId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable : " + dto.getAssigneeId()));
            task.setAssignee(assignee);
        } else {
            task.setAssignee(null);
        }
    }
}
