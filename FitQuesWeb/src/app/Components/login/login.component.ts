// src/app/login/login.component.ts
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../Services/login/auth-service.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'] 
})
export class LoginComponent {
  
  username: string = ''; 
  password: string = ''; 
  errorMessage: string = ''; 

  constructor(private authService: AuthService, private router: Router) { }


  onLoginSubmit(): void {
    if (!this.username.trim() || !this.password.trim()) {
      this.errorMessage = 'Por favor, introduce usuario y contraseña.';
      return;
    }

    this.errorMessage = ''; // Limpiar mensaje de error previo

    this.authService.login({ username: this.username, password: this.password }).subscribe({
      next: (response) => {
        console.log('Login exitoso:', response);
        this.router.navigate(['/inicio']); // Redirigir a la página de inicio
      },
      error: (err) => {
        console.error('Error en el login:', err);
        // Aquí puedes mejorar el mensaje de error según la estructura de tu 'err'
        this.errorMessage = err.message || 'Ocurrió un error desconocido. Inténtalo de nuevo.';
      }
    });
  }

  onRegisterClick(): void {
    console.log('Redirigiendo a la página de registro...');
    this.router.navigate(['/register']); 
  }
}