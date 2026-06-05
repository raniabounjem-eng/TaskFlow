import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';

// Angular Material
import { MatCardModule }            from '@angular/material/card';
import { MatFormFieldModule }       from '@angular/material/form-field';
import { MatInputModule }           from '@angular/material/input';
import { MatButtonModule }          from '@angular/material/button';
import { MatIconModule }            from '@angular/material/icon';
import { MatChipsModule }           from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatToolbarModule }         from '@angular/material/toolbar';
import { MatSidenavModule }         from '@angular/material/sidenav';
import { MatListModule }            from '@angular/material/list';
import { MatTableModule }           from '@angular/material/table';
import { MatSnackBarModule }        from '@angular/material/snack-bar';
import { MatMenuModule }            from '@angular/material/menu';

// Angular CDK (Drag & Drop pour le Kanban)
import { DragDropModule } from '@angular/cdk/drag-drop';

// Routing
import { AppRoutingModule } from './app-routing.module';

// Composants
import { AppComponent }       from './app.component';
import { LoginComponent }     from './features/auth/login/login.component';
import { TaskBoardComponent } from './features/tasks/task-board/task-board.component';

// Interceptors
import { JwtInterceptor } from './core/interceptors/jwt.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    TaskBoardComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    ReactiveFormsModule,
    AppRoutingModule,
    DragDropModule,
    // Angular Material modules
    MatCardModule, MatFormFieldModule, MatInputModule,
    MatButtonModule, MatIconModule, MatChipsModule,
    MatProgressSpinnerModule, MatToolbarModule,
    MatSidenavModule, MatListModule, MatTableModule,
    MatSnackBarModule, MatMenuModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
