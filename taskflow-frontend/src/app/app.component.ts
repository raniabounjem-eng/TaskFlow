import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './core/services/auth.service';

@Component({
  standalone: false,
  selector: 'app-root',
  template: `
    <ng-container *ngIf="authService.isLoggedIn$ | async; else noNav">
      <mat-toolbar color="primary" class="app-toolbar">
        <span>📋 TaskFlow</span>
        <span class="spacer"></span>
        <span class="user-name">{{ (authService.getCurrentUser())?.fullName }}</span>
        <button mat-icon-button (click)="logout()" title="Se déconnecter">
          <mat-icon>logout</mat-icon>
        </button>
      </mat-toolbar>
      <div class="main-content">
        <router-outlet></router-outlet>
      </div>
    </ng-container>
    <ng-template #noNav>
      <router-outlet></router-outlet>
    </ng-template>
  `,
  styles: [`
    .spacer { flex: 1 1 auto; }
    .user-name { margin-right: 1rem; font-size: 0.9rem; }
  `]
})
export class AppComponent implements OnInit {
  constructor(public authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    if (!this.authService.getToken()) {
      this.router.navigate(['/login']);
    }
  }

  logout(): void { this.authService.logout(); }
}
