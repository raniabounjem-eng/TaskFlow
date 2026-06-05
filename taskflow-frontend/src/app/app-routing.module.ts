import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent }     from './features/auth/login/login.component';
import { TaskBoardComponent } from './features/tasks/task-board/task-board.component';
import { AuthGuard }          from './core/guards/auth.guard';

const routes: Routes = [
  { path: '',          redirectTo: 'login', pathMatch: 'full' },
  { path: 'login',     component: LoginComponent },
  {
    path: 'dashboard',
    component: TaskBoardComponent,
    canActivate: [AuthGuard],
    data: { projectId: 1 }   // projet par défaut – sprint 1
  },
  { path: '**', redirectTo: 'login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
