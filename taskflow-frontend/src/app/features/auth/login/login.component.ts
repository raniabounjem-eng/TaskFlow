import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  standalone: false,
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent {

  loginForm: FormGroup;
  errorMessage = '';
  loading      = false;
  hidePassword = true;

  constructor(
    private fb:          FormBuilder,
    private authService: AuthService,
    private router:      Router
  ) {
    this.loginForm = this.fb.group({
      email:    ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) return;

    this.loading      = true;
    this.errorMessage = '';

    this.authService.login(this.loginForm.value).subscribe({
      next:  ()    => this.router.navigate(['/dashboard']),
      error: ()    => {
        this.errorMessage = 'Email ou mot de passe incorrect';
        this.loading      = false;
      }
    });
  }

  get email()    { return this.loginForm.get('email'); }
  get password() { return this.loginForm.get('password'); }
}
