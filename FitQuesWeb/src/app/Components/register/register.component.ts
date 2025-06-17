import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, RegisterRequest } from '../../Services/login/auth-service.service';
import { Genero } from '../../models/genero.model';



@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  nombre: string = '';
  apellido1: string = '';
  apellido2: string = '';
  email: string = '';
  username: string = '';
  password: string = '';
  verifyPassword: string = '';
  peso:  number | null = null;
  altura:  number | null = null;
  edad:  number | null = null;
  genero: Genero | null = null;
  nivelId: number | null = null;

  errorMessage: string = '';
  successMessage: string = '';

  generos = Object.values(Genero);

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
  }

  onRegisterSubmit(): void {
    // Validaciones de NotBlank y Size (min/max)
    if (this.nombre.trim().length < 2 || this.nombre.trim().length > 50) {
      this.errorMessage = 'El nombre debe tener entre 2 y 50 caracteres.';
      return;
    }
    if (this.apellido1.trim().length < 2 || this.apellido1.trim().length > 50) {
      this.errorMessage = 'El primer apellido debe tener entre 2 y 50 caracteres.';
      return;
    }
    // apellido2 es opcional, solo validar si no está vacío
    if (this.apellido2.trim() !== '' && (this.apellido2.trim().length < 2 || this.apellido2.trim().length > 50)) {
      this.errorMessage = 'El segundo apellido, si se proporciona, debe tener entre 2 y 50 caracteres.';
      return;
    }

    // Validación de email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.email)) {
      this.errorMessage = 'El email debe ser válido.';
      return;
    }

    // Validaciones de username
    if (this.username.trim().length < 4 || this.username.trim().length > 20) {
      this.errorMessage = 'El nombre de usuario debe tener entre 4 y 20 caracteres.';
      return;
    }

    // Validaciones de contraseña
    if (this.password.length < 6 || this.password.length > 20) {
      this.errorMessage = 'La contraseña debe tener entre 6 y 20 caracteres.';
      return;
    }
    if (this.password !== this.verifyPassword) {
      this.errorMessage = 'Las contraseñas no coinciden.';
      return;
    }

    // Validaciones numéricas (NotNull y Positive)
    if (this.peso === null || this.peso <= 0) {
      this.errorMessage = 'El peso debe ser un número positivo.';
      return;
    }
    if (this.altura === null || this.altura <= 0) {
      this.errorMessage = 'La altura debe ser un número positivo.';
      return;
    }
    // Asegurarse de que edad sea un número y positivo
    if (this.edad === null || this.edad <= 0) {
      this.errorMessage = 'La edad debe ser un número positivo.';
      return;
    }

    // Validación de género
    if (this.genero === null ) {
      this.errorMessage = 'Debe seleccionar un género.';
      return;
    }

    // Validación de nivelId
    if (this.nivelId === null || (this.nivelId !== 1 && this.nivelId !== 2)) {
      this.errorMessage = 'Debe seleccionar un nivel de entrenamiento (Principiante o Intermedio).';
      return;
    }

    // Si todo es válido, procede con el registro
    this.errorMessage = '';
    this.successMessage = '';

    const registerData: RegisterRequest = {
      nombre: this.nombre,
      apellido1: this.apellido1,
      apellido2: this.apellido2,
      email: this.email,
      username: this.username,
      password: this.password,
      verifyPassword: this.verifyPassword,
      peso: this.peso,
      altura: this.altura,
      edad: this.edad,
      genero: this.genero,
      nivelId: this.nivelId
    };

    this.authService.register(registerData).subscribe({
      next: (response) => {
        console.log('Registro exitoso:', response);
        this.successMessage = '¡Registro exitoso! Por favor, verifica tu email para activar tu cuenta.';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 3000);
      },
      error: (err) => {
        console.error('Error en el registro:', err);
        // Mostrar el mensaje de error del backend si está disponible
        this.errorMessage = err.error?.message || err.message || 'Ocurrió un error desconocido durante el registro. Inténtalo de nuevo.';
      }
    });
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }
}