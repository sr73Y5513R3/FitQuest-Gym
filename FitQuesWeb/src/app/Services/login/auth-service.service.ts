// src/app/services/auth/auth.service.ts

import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, BehaviorSubject, throwError } from 'rxjs';
import { tap, catchError, map } from 'rxjs/operators';
import { Router } from '@angular/router';

interface LoginResponse {
  id: string;
  username: string;
  token: string;
  refreshToken: string;
  roles: string[];
}

interface RefreshTokenResponse {
  id: string;
  username: string;
  token: string;
  refreshToken: string;
  roles: string[];
}

export interface RegisterRequest {
  nombre: string;
  apellido1: string;
  apellido2: string;
  email: string;
  username: string;
  password: string;
  verifyPassword: string;
  peso: number; 
  altura: number; 
  edad: number; 
  genero: string;
  nivelId: number;
}

export interface RegisterResponse {
  id: string;
  username: string;
  roles: string[];
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly AUTH_API_BASE_URL = 'http://localhost:8080/auth';

  private readonly TOKEN_KEY = 'jwt_token';
  private readonly REFRESH_TOKEN_KEY = 'refresh_token';
  private readonly USERNAME_KEY = 'username';
  private readonly USER_ID_KEY = 'user_id';
  private readonly USER_ROLES_KEY = 'user_roles';

  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasToken());
  isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  private userRolesSubject = new BehaviorSubject<string[]>(this.getStoredRoles());
  userRoles$: Observable<string[]> = this.userRolesSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    this.isAuthenticatedSubject.next(this.hasToken());
    this.userRolesSubject.next(this.getStoredRoles());
  }

  private hasToken(): boolean {
    return !!localStorage.getItem(this.TOKEN_KEY);
  }

  private getStoredRoles(): string[] {
    const rolesJson = localStorage.getItem(this.USER_ROLES_KEY);
    try {
      return rolesJson ? JSON.parse(rolesJson) : [];
    } catch (e) {
      console.error('Error al parsear los roles almacenados:', e);
      return [];
    }
  }

  login(credentials: { username: string; password: string }): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.AUTH_API_BASE_URL}/login`, credentials).pipe(
      tap(response => {
        localStorage.setItem(this.TOKEN_KEY, response.token);
        localStorage.setItem(this.REFRESH_TOKEN_KEY, response.refreshToken);
        localStorage.setItem(this.USERNAME_KEY, response.username);
        localStorage.setItem(this.USER_ID_KEY, response.id);
        localStorage.setItem(this.USER_ROLES_KEY, JSON.stringify(response.roles));

        this.isAuthenticatedSubject.next(true);
        this.userRolesSubject.next(response.roles);
      }),
      catchError(this.handleError)
    );
  }

  refreshToken(): Observable<RefreshTokenResponse> {
    const currentRefreshToken = this.getRefreshToken();

    if (!currentRefreshToken) {
      this.logout();
      return throwError(() => new Error('No refresh token available.'));
    }

    return this.http.post<RefreshTokenResponse>(`${this.AUTH_API_BASE_URL}/refresh-token`, { refreshToken: currentRefreshToken }).pipe(
      tap(response => {
        localStorage.setItem(this.TOKEN_KEY, response.token);
        localStorage.setItem(this.REFRESH_TOKEN_KEY, response.refreshToken);
        localStorage.setItem(this.USERNAME_KEY, response.username);
        localStorage.setItem(this.USER_ID_KEY, response.id);
        localStorage.setItem(this.USER_ROLES_KEY, JSON.stringify(response.roles));

        this.isAuthenticatedSubject.next(true);
        this.userRolesSubject.next(response.roles);
      }),
      catchError(err => {
        console.error('Error al refrescar el token:', err);
        this.logout();
        return throwError(() => new Error('Refresh token failed or expired.'));
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.REFRESH_TOKEN_KEY);
    localStorage.removeItem(this.USERNAME_KEY);
    localStorage.removeItem(this.USER_ID_KEY);
    localStorage.removeItem(this.USER_ROLES_KEY);
    this.isAuthenticatedSubject.next(false);
    this.userRolesSubject.next([]);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  getRefreshToken(): string | null {
    return localStorage.getItem(this.REFRESH_TOKEN_KEY);
  }

  getUsername(): string | null {
    return localStorage.getItem(this.USERNAME_KEY);
  }

  getUserId(): string | null {
    return localStorage.getItem(this.USER_ID_KEY);
  }

  isAdmin(): Observable<boolean> {
    return this.userRoles$.pipe(
      map(roles => roles.includes('ADMIN') || roles.includes('ROLE_ADMIN'))
    );
  }

  isEntrenador(): Observable<boolean> {
    return this.userRoles$.pipe(
      map(roles => roles.includes('ENTRENADOR') || roles.includes('ROLE_ENTRENADOR'))
    );
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Ocurrió un error desconocido. Por favor, inténtalo de nuevo.';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Error del cliente: ${error.error.message}`;
    } else {
      if (error.status === 401 || error.status === 403) {
        errorMessage = 'Credenciales inválidas. Por favor, verifica tu usuario y contraseña.';
      } else if (error.error && typeof error.error === 'object' && error.error.message) {
        errorMessage = error.error.message;
      } else if (error.error && typeof error.error === 'string') {
        errorMessage = error.error;
      } else {
        errorMessage = `Error del servidor: ${error.status} - ${error.message}`;
      }
    }
    console.error('Error en la API de Autenticación:', errorMessage);
    return throwError(() => new Error(errorMessage));
  }

  register(registerData: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(`${this.AUTH_API_BASE_URL}/register/cliente`, registerData).pipe(
      catchError(this.handleError)
    );
  }

  getLoggedInUserId(): string | null {
    return localStorage.getItem(this.USER_ID_KEY);
  }
}