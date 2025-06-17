import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, OnDestroy } from '@angular/core'; // Asegúrate de importar OnInit y OnDestroy
import { RealizaService } from '../../Services/realiza/realiza.service';
import { AuthService } from '../../Services/login/auth-service.service';
import { Subscription } from 'rxjs';
import { GetRealizaDto, Page } from '../../models/realiza.model';

@Component({
  selector: 'app-entrenamientos-realizados', // Confirmar si es este selector o 'app-mis-entrenamientos-realizados'
  templateUrl: './entrenamientos-realizados.component.html',
  styleUrls: ['./entrenamientos-realizados.component.css'] // styleUrl debe ser styleUrls
})
export class EntrenamientosRealizadosComponent implements OnInit, OnDestroy { // Implementa OnInit y OnDestroy

  realizasPage: Page<GetRealizaDto> | undefined;
  currentPage: number = 0;
  pageSize: number = 10;

  userId: string | null = null;
  errorMessage: string | null = null;

  private authSubscription: Subscription | undefined;

  constructor(
    private realizaService: RealizaService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.authSubscription = this.authService.isAuthenticated$.subscribe(isLoggedIn => {
      if (isLoggedIn) {
        this.userId = this.authService.getLoggedInUserId();
        if (this.userId) {
          this.loadUserRealizas();
        } else {
          this.errorMessage = 'No se pudo obtener el ID del usuario. Por favor, inicia sesión de nuevo.';
        }
      } else {
        this.userId = null;
        this.realizasPage = undefined;
        this.errorMessage = 'Debes iniciar sesión para ver tus entrenamientos realizados.';
      }
    });
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  loadUserRealizas(): void {
    if (!this.userId) {
      this.errorMessage = 'ID de usuario no disponible. Asegúrate de estar logueado.';
      return;
    }

    this.realizaService.getAllByUser(this.userId, this.currentPage, this.pageSize)
      .subscribe({
        next: (data) => {
          this.realizasPage = data;
          console.log('Entrenamientos realizados del usuario cargados:', this.realizasPage);
          this.errorMessage = null; // Limpiar mensaje de error
        },
        error: (err: HttpErrorResponse) => {
          console.error('Error al cargar entrenamientos realizados del usuario:', err);
          if (err.status === 401 || err.status === 403) {
            this.errorMessage = 'No tienes permiso para ver esta información o tu sesión ha expirado.';
            // Podrías redirigir al login aquí si es un error de autenticación crítico
          } else if (err.status === 404) {
             this.errorMessage = 'No se encontraron entrenamientos realizados para este usuario.';
             // CORRECCIÓN AQUÍ: Ajustar la propiedad 'pageable' a tu definición exacta
             this.realizasPage = {
                 content: [],
                 empty: true,
                 first: true,
                 last: true,
                 number: 0,
                 numberOfElements: 0,
                 size: this.pageSize,
                 totalElements: 0,
                 totalPages: 0,
                 // *** Propiedad 'pageable' ajustada a tu modelo ***
                 pageable: {
                     pageNumber: this.currentPage,
                     pageSize: this.pageSize
                 }
             };
          } else {
            this.errorMessage = 'Error al cargar tus entrenamientos realizados. Inténtalo de nuevo.';
          }
          setTimeout(() => this.errorMessage = null, 5000);
        }
      });
  }

  goToPage(page: number): void {
    this.currentPage = page;
    this.loadUserRealizas();
  }

  nextPage(): void {
    if (this.realizasPage && !this.realizasPage.last) {
      this.currentPage++;
      this.loadUserRealizas();
    }
  }

  previousPage(): void {
    if (this.realizasPage && !this.realizasPage.first) {
      this.currentPage--;
      this.loadUserRealizas();
    }
  }
}