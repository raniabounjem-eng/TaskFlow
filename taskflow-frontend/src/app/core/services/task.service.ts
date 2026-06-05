import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Task, TaskDTO, TaskStatus } from '../models/task.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class TaskService {

  private readonly url = `${environment.apiUrl}/tasks`;

  constructor(private http: HttpClient) {}

  getByProject(projectId: number): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.url}/project/${projectId}`);
  }

  getByProjectAndStatus(projectId: number, status: TaskStatus): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.url}/project/${projectId}/status/${status}`);
  }

  getMyTasks(userId: number): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.url}/my-tasks/${userId}`);
  }

  getById(id: number): Observable<Task> {
    return this.http.get<Task>(`${this.url}/${id}`);
  }

  create(dto: TaskDTO): Observable<Task> {
    return this.http.post<Task>(this.url, dto);
  }

  update(id: number, dto: TaskDTO): Observable<Task> {
    return this.http.put<Task>(`${this.url}/${id}`, dto);
  }

  updateStatus(id: number, status: TaskStatus): Observable<Task> {
    return this.http.patch<Task>(`${this.url}/${id}/status`, { status });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
