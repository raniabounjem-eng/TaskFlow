// ─── task.model.ts ────────────────────────────────────────────────────────────
export type TaskStatus   = 'TODO' | 'IN_PROGRESS' | 'REVIEW' | 'DONE';
export type TaskPriority = 'LOW'  | 'MEDIUM' | 'HIGH' | 'CRITICAL';

export interface Task {
  id:           number;
  title:        string;
  description:  string;
  status:       TaskStatus;
  priority:     TaskPriority;
  dueDate:      string | null;
  createdAt:    string;
  updatedAt:    string;
  projectId:    number;
  assigneeId:   number | null;
  assigneeName: string | null;
}

export interface TaskDTO {
  title:       string;
  description: string;
  status:      TaskStatus;
  priority:    TaskPriority;
  dueDate:     string | null;
  projectId:   number;
  assigneeId:  number | null;
}
