package ma.rania.taskflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ma.rania.taskflow.model.Task;

import java.time.LocalDate;

@Data
public class TaskDTO {

    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    private String description;

    @NotNull(message = "Le statut est obligatoire")
    private Task.Status status;

    @NotNull(message = "La priorité est obligatoire")
    private Task.Priority priority;

    private LocalDate dueDate;

    @NotNull(message = "Le projet est obligatoire")
    private Long projectId;

    private Long assigneeId;
}
