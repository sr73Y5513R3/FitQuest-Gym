import { Component, OnInit } from '@angular/core';
import { EntrenamientoService } from '../../Services/entrenamiento/entrenamiento.service';
import { GetEntrenoConEjercicioDto, Page } from '../../models/entrenamiento.model';
import { Router } from '@angular/router';
import { AuthService } from '../../Services/login/auth-service.service';
import { HttpErrorResponse } from '@angular/common/http';

// Ya no necesitamos la interfaz ValoracionBody aquí, ya que el servicio construye el body completo.
// La interfaz CreateValoracionCommand ya está definida o se definirá en el servicio.

@Component({
  selector: 'app-entrenamiento',
  templateUrl: './entrenamiento.component.html',
  styleUrl: './entrenamiento.component.css'
})
export class EntrenamientoComponent implements OnInit {

  entrenamientosPage: Page<GetEntrenoConEjercicioDto> | undefined;
  currentPage: number = 0;
  pageSize: number = 6;
  searchTerm: string = '';

  successMessage: string | null = null;
  errorMessage: string | null = null;

  selectedRating: number = 0; 
  showRatingInput: boolean = false; 
  currentEntrenoIdToRate: number | null = null; 

  constructor(
    private entrenamientoService: EntrenamientoService,
    private router: Router,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.loadEntrenamientos();
  }

  loadEntrenamientos(): void {
    this.entrenamientoService.findAllEntrenamientos(this.currentPage, this.pageSize, this.searchTerm)
      .subscribe({
        next: (data) => {
          this.entrenamientosPage = data;
          console.log('Entrenamientos cargados:', this.entrenamientosPage);
          this.errorMessage = null;
        },
        error: (err) => {
          console.error('Error al cargar entrenamientos:', err);
          this.errorMessage = 'No se pudieron cargar los entrenamientos. Inténtalo de nuevo.';
          setTimeout(() => this.errorMessage = null, 5000);
        }
      });
  }

  goToPage(page: number): void {
    this.currentPage = page;
    this.loadEntrenamientos();
  }

  nextPage(): void {
    if (this.entrenamientosPage && !this.entrenamientosPage.last) {
      this.currentPage++;
      this.loadEntrenamientos();
    }
  }

  previousPage(): void {
    if (this.entrenamientosPage && !this.entrenamientosPage.first) {
      this.currentPage--;
      this.loadEntrenamientos();
    }
  }

  onSearchChange(): void {
    this.currentPage = 0;
    this.loadEntrenamientos();
  }

  navigateToEntrenamientoDetails(id: number): void {
    this.router.navigate(['/entrenamientos', id]);
  }

 
  startRating(entrenoId: number): void {
    this.currentEntrenoIdToRate = entrenoId;
    this.selectedRating = 0; 
    this.showRatingInput = true; 
    this.successMessage = null;
    this.errorMessage = null;
  }

  setRating(rating: number): void {
    this.selectedRating = rating;
  }

  submitRating(): void {
    this.successMessage = null;
    this.errorMessage = null;

    if (this.currentEntrenoIdToRate === null || this.selectedRating === 0) {
      this.errorMessage = 'Por favor, selecciona una puntuación antes de enviar.';
      setTimeout(() => this.errorMessage = null, 3000);
      return;
    }

    const userId = this.authService.getLoggedInUserId();
    if (!userId) {
      this.errorMessage = 'No se pudo obtener el ID del usuario. Por favor, inicia sesión.';
      setTimeout(() => this.errorMessage = null, 5000);
      this.router.navigate(['/login']);
      this.cancelRating(); 
      return;
    }

    this.entrenamientoService.addValoracion(userId, this.currentEntrenoIdToRate, this.selectedRating).subscribe({
      next: (response) => {
        this.successMessage = '¡Gracias! Tu valoración ha sido guardada con éxito.';
        console.log('Valoración añadida:', response);
        this.loadEntrenamientos(); 
        setTimeout(() => this.successMessage = null, 3000);
        this.cancelRating();
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error al añadir valoración:', err);
        if (err.status === 409) {
          this.errorMessage = 'Ya has valorado este entrenamiento. Solo se permite una valoración por usuario.';
        } else if (err.status === 404) {
          this.errorMessage = 'Entrenamiento o usuario no encontrado.';
        } else if (err.status === 400) {
          this.errorMessage = 'Solicitud de valoración inválida. Asegúrate de que los datos son correctos.';
        } else {
          this.errorMessage = 'Hubo un error al guardar tu valoración. Inténtalo de nuevo más tarde.';
        }
        setTimeout(() => this.errorMessage = null, 5000);
      }
    });
  }

  cancelRating(): void {
    this.showRatingInput = false;
    this.selectedRating = 0;
    this.currentEntrenoIdToRate = null;
  }
}