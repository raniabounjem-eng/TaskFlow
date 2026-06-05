// ─── user.model.ts ────────────────────────────────────────────────────────────
export interface User {
  id: number;
  fullName: string;
  email: string;
  role: 'USER' | 'MANAGER' | 'ADMIN';
}

export interface LoginRequest  { email: string; password: string; }
export interface LoginResponse { token: string; email: string; fullName: string; role: string; }
