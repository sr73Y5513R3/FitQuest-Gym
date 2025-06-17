import { Component, OnInit } from '@angular/core';
import { EditClienteCmd, GetClienteDto } from '../../models/usuario.model';
import { Subscription } from 'rxjs';
import { AuthService } from '../../Services/login/auth-service.service';
import { UsuarioService } from '../../Services/usuario/usuario.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Genero } from '../../models/genero.model';

@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.css'
})
export class PerfilComponent implements OnInit{

  editClientData: EditClienteCmd = { 
    nombre: '',
    apellido1: '',
    apellido2: '',
    email: '',
    username: '',
    peso: 0,
    altura: 0,
    edad: 0,
    genero: ''
  }
  cliente: GetClienteDto | undefined;
  userId: string | null = null;
  loading: boolean = true;
  errorMessage: string | null = null;
  isEditing: boolean = false;
  successMessage: string | null = null;
  generos = Object.values(Genero);
  

  private authSubscription: Subscription | undefined;
  private clienteSubscription: Subscription | undefined;
  private updateSubscription: Subscription | undefined;

  constructor(
    private authService: AuthService,
    private clienteService: UsuarioService
  ) { }

  ngOnInit(): void {
    this.authSubscription = this.authService.isAuthenticated$.subscribe(isLoggedIn => {
      if (isLoggedIn) {
        this.userId = this.authService.getLoggedInUserId();
        if (this.userId) {
          this.loadClienteData();
        } else {
          this.errorMessage = 'No se pudo obtener el ID del usuario logueado.';
          this.loading = false;
        }
      } else {
        this.userId = null;
        this.cliente = undefined;
        this.errorMessage = 'Debes iniciar sesión para ver tu perfil.';
        this.loading = false;
      }
    });
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
    if (this.clienteSubscription) {
      this.clienteSubscription.unsubscribe();
    }
  }

  loadClienteData(): void {
    if (!this.userId) {
      this.errorMessage = 'ID de usuario no disponible.';
      this.loading = false;
      return;
    }

    this.loading = true;
    this.errorMessage = null;

    this.clienteSubscription = this.clienteService.getClienteById(this.userId)
      .subscribe({
        next: (data) => {
          this.cliente = data;
          this.loading = false;
          console.log('Datos del cliente cargados:', this.cliente);
        },
        error: (err: HttpErrorResponse) => {
          console.error('Error al cargar los datos del cliente:', err);
          this.loading = false;
          if (err.status === 404) {
            this.errorMessage = 'Los datos de tu perfil no fueron encontrados.';
          } else if (err.status === 401 || err.status === 403) {
            this.errorMessage = 'No tienes permiso para ver esta información o tu sesión ha expirado.';
          } else {
            this.errorMessage = 'Error al cargar los datos de tu perfil. Inténtalo de nuevo.';
          }
        }
      });
  }

  toggleEditMode(): void {
    this.isEditing = !this.isEditing;
    if (this.isEditing && this.cliente) {
      this.editClientData = { ...this.cliente };
    } else {
      
      this.loadClienteData();
    }
    this.errorMessage = null; 
    this.successMessage = null;
  }

  saveProfile(): void {
    if (!this.userId) {
      this.errorMessage = 'Error: ID de usuario no disponible para guardar cambios.';
      return;
    }

    this.loading = true;
    this.errorMessage = null;
    this.successMessage = null;

    this.updateSubscription = this.clienteService.updateCliente(this.userId, this.editClientData)
      .subscribe({
        next: (updatedClient) => {
          this.cliente = updatedClient;
          this.loading = false;
          this.isEditing = false; 
          this.successMessage = 'Perfil actualizado exitosamente.';
          setTimeout(() => this.successMessage = null, 3000); 
          console.log('Perfil actualizado:', updatedClient);
        },
        error: (err: HttpErrorResponse) => {
          console.error('Error al actualizar el perfil:', err);
          this.loading = false;
          this.errorMessage = err.message || 'Hubo un error al actualizar tu perfil. Inténtalo de nuevo.';
          setTimeout(() => this.errorMessage = null, 5000);
        }
      });
  }

  cancelEdit(): void {
    this.isEditing = false;
    this.loadClienteData(); // Recargar los datos originales para descartar cambios
    this.errorMessage = null;
    this.successMessage = null;
  }
  
}
