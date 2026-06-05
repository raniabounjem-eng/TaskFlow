package ma.rania.taskflow.dto;

import lombok.Data;
import ma.rania.taskflow.model.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private Task.Status status;
    private Task.Priority priority;
    private LocalDate dueDate;
    private Long projectId;
    private Long assigneeId;
    private String assigneeName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TaskResponse from(Task task) {
        TaskResponse r = new TaskResponse();
        r.id          = task.getId();
        r.title       = task.getTitle();
        r.description = task.getDescription();
        r.status      = task.getStatus();
        r.priority    = task.getPriority();
        r.dueDate     = task.getDueDate();
        r.projectId   = task.getProject().getId();
        r.createdAt   = task.getCreatedAt();
        r.updatedAt   = task.getUpdatedAt();
        if (task.getAssignee() != null) {
            r.assigneeId   = task.getAssignee().getId();
            r.assigneeName = task.getAssignee().getFullName();
        }
        return r;
    }
}
