package ma.rania.taskflow.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.rania.taskflow.dto.TaskDTO;
import ma.rania.taskflow.dto.TaskResponse;
import ma.rania.taskflow.model.Task;
import ma.rania.taskflow.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/project/{projectId}")
    public List<TaskResponse> getByProject(@PathVariable Long projectId) {
        return taskService.getByProject(projectId);
    }

    @GetMapping("/project/{projectId}/status/{status}")
    public List<TaskResponse> getByProjectAndStatus(
            @PathVariable Long projectId,
            @PathVariable Task.Status status) {
        return taskService.getByProjectAndStatus(projectId, status);
    }

    @GetMapping("/my-tasks/{userId}")
    public List<TaskResponse> getMyTasks(@PathVariable Long userId) {
        return taskService.getMyTasks(userId);
    }

    @GetMapping("/{id}")
    public TaskResponse getById(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(dto));
    }

    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable Long id, @Valid @RequestBody TaskDTO dto) {
        return taskService.update(id, dto);
    }

    @PatchMapping("/{id}/status")
    public TaskResponse updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return taskService.updateStatus(id, body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
