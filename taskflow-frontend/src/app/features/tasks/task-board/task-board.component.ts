import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Task, TaskStatus } from '../../../core/models/task.model';
import { TaskService } from '../../../core/services/task.service';

@Component({
  standalone: false,
  selector: 'app-task-board',
  templateUrl: './task-board.component.html'
})
export class TaskBoardComponent implements OnInit {

  projectId!: number;

  columns: { id: TaskStatus; label: string; color: string; tasks: Task[] }[] = [
    { id: 'TODO',        label: '📝 À faire',     color: '#e3f2fd', tasks: [] },
    { id: 'IN_PROGRESS', label: '⚙️ En cours',    color: '#fff3e0', tasks: [] },
    { id: 'REVIEW',      label: '🔍 En révision', color: '#f3e5f5', tasks: [] },
    { id: 'DONE',        label: '✅ Terminé',      color: '#e8f5e9', tasks: [] }
  ];

  constructor(
    private taskService: TaskService,
    private route:       ActivatedRoute
  ) {}

  ngOnInit(): void {
    // projectId passé en data: { projectId: 1 } dans les routes
    this.projectId = this.route.snapshot.data['projectId'] ?? 1;
    this.loadTasks();
  }

  loadTasks(): void {
    this.taskService.getByProject(this.projectId).subscribe(tasks => {
      this.columns.forEach(col => {
        col.tasks = tasks.filter(t => t.status === col.id);
      });
    });
  }

  drop(event: CdkDragDrop<Task[]>, newStatus: TaskStatus): void {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
      const movedTask = event.container.data[event.currentIndex];
      this.taskService.updateStatus(movedTask.id, newStatus).subscribe({
        error: () => this.loadTasks()   // rollback si erreur API
      });
    }
  }

  getPriorityColor(priority: string): string {
    const colors: Record<string, string> = {
      LOW: '#4caf50', MEDIUM: '#ff9800', HIGH: '#f44336', CRITICAL: '#9c27b0'
    };
    return colors[priority] ?? '#9e9e9e';
  }

  getColumnIds(): string[] {
    return this.columns.map(c => c.id);
  }
}
