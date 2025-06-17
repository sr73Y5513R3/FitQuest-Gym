import { Component, OnInit } from '@angular/core';
import { EntrenamientoService } from '../../Services/entrenamiento/entrenamiento.service';
import { GetEntrenoConEjercicioDto, Page } from '../../models/entrenamiento.model';
import { Router } from '@angular/router';
import { AuthService } from '../../Services/login/auth-service.service';
import { HttpErrorResponse } from '@angular/common/http';
import { RealizaService } from '../../Services/realiza/realiza.service';
import { CreateRealizaCmd } from '../../models/realiza.model';

@Component({
  selector: 'app-entrenamiento',
  templateUrl: './entrenamiento.component.html',
  styleUrl: './entrenamiento.component.css'
})
export class EntrenamientoComponent implements OnInit {

  allEntrenamientos: GetEntrenoConEjercicioDto[] = []; // Todos los entrenamientos (desde backend, filtrados por searchTerm)
  
  entrenamientosPrincipiantes: GetEntrenoConEjercicioDto[] = [];
  entrenamientosIntermedios: GetEntrenoConEjercicioDto[] = [];

  currentDisplayedList: GetEntrenoConEjercicioDto[] = [];

  currentPage: number = 0;
  pageSize: number = 6;
  searchTerm: string = '';

  activeGallery: 'all' | 'principiante' | 'intermedio' = 'all';

  successMessage: string | null = null;
  errorMessage: string | null = null;

  selectedRating: number = 0;
  showRatingInput: boolean = false;
  currentEntrenoIdToRate: number | null = null;

  totalPages: number = 0;
  isFirstPage: boolean = true;
  isLastPage: boolean = true;


  constructor(
    private entrenamientoService: EntrenamientoService,
    private router: Router,
    private authService: AuthService,
    private realizaService: RealizaService
  ) { }

  ngOnInit(): void {
    this.loadEntrenamientos();
  }

  loadEntrenamientos(): void {

    this.entrenamientoService.findAllEntrenamientos(0, 1000, this.searchTerm)
      .subscribe({
        next: (pageData) => {
          this.allEntrenamientos = pageData.content; 
          this.distributeEntrenamientosByLevel(); 
          this.applyPaginationToCurrentGallery(); 
          this.errorMessage = null;
        },
        error: (err) => {
          console.error('Error al cargar entrenamientos:', err);
          this.errorMessage = 'No se pudieron cargar los entrenamientos. Inténtalo de nuevo.';
          setTimeout(() => this.errorMessage = null, 5000);
        }
      });
  }

  distributeEntrenamientosByLevel(): void {
    this.entrenamientosPrincipiantes = this.allEntrenamientos.filter(entreno =>
      entreno.nivel.nombre.toUpperCase() === 'PRINCIPIANTE'
    );
    this.entrenamientosIntermedios = this.allEntrenamientos.filter(entreno =>
      entreno.nivel.nombre.toUpperCase() === 'INTERMEDIO'
    );
    // Puedes añadir más filtros si tienes más niveles (AVANZADO, etc.)
  }

  applyPaginationToCurrentGallery(): void {
    let sourceList: GetEntrenoConEjercicioDto[];

    switch (this.activeGallery) {
      case 'principiante':
        sourceList = this.entrenamientosPrincipiantes;
        break;
      case 'intermedio':
        sourceList = this.entrenamientosIntermedios;
        break;
      case 'all':
      default:
        sourceList = this.allEntrenamientos; // Cuando se muestran todos
        break;
    }

    this.currentDisplayedList = sourceList; // Establecer la lista base para paginar

    // Calcular paginación sobre la lista seleccionada
    this.totalPages = Math.ceil(this.currentDisplayedList.length / this.pageSize);
    if (this.currentPage >= this.totalPages && this.totalPages > 0) {
      this.currentPage = this.totalPages - 1;
    } else if (this.totalPages === 0) {
        this.currentPage = 0; // Si no hay entrenamientos, la página es 0
    }

    const startIndex = this.currentPage * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.currentDisplayedList = this.currentDisplayedList.slice(startIndex, endIndex);

    this.isFirstPage = this.currentPage === 0;
    this.isLastPage = this.currentPage >= this.totalPages - 1 || this.totalPages === 0;
  }


  // Método para cambiar la galería activa
  showGallery(galleryType: 'all' | 'principiante' | 'intermedio'): void {
    this.activeGallery = galleryType;
    this.currentPage = 0; // Reiniciar a la primera página al cambiar de galería
    this.applyPaginationToCurrentGallery();
  }

  goToPage(page: number): void {
    this.currentPage = page;
    this.applyPaginationToCurrentGallery();
  }

  nextPage(): void {
    if (!this.isLastPage) {
      this.currentPage++;
      this.applyPaginationToCurrentGallery();
    }
  }

  previousPage(): void {
    if (!this.isFirstPage) {
      this.currentPage--;
      this.applyPaginationToCurrentGallery();
    }
  }

  onSearchChange(): void {
    this.currentPage = 0; // Reiniciar paginación
    this.loadEntrenamientos(); // Recargar todos los datos desde el backend con el nuevo término de búsqueda
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
        // Recargar para que se actualicen las valoraciones medias y se refleje en la UI
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

  realizarEntrenamiento(entrenoId: number): void {
    this.successMessage = null;
    this.errorMessage = null;

    const userId = this.authService.getLoggedInUserId();
    if (!userId) {
      this.errorMessage = 'Necesitas iniciar sesión para realizar un entrenamiento.';
      setTimeout(() => this.errorMessage = null, 5000);
      this.router.navigate(['/login']);
      return;
    }

    const realizaCmd: CreateRealizaCmd = {
      idUsuario: userId,
      idEntrenamiento: entrenoId
    };

    this.realizaService.entrenamientoRealizado(realizaCmd).subscribe({
      next: (response) => {
        this.successMessage = '¡Entrenamiento realizado con éxito! Un entrenador lo revisará pronto.';
        console.log('Entrenamiento realizado:', response);
        // Recargar para que se actualice el estado o contadores si hubiera
        this.loadEntrenamientos();
        setTimeout(() => this.successMessage = null, 5000);
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error al realizar entrenamiento:', err);
        if (err.status === 409) {
          this.errorMessage = 'Ya has marcado este entrenamiento como realizado.';
        } else if (err.status === 404) {
          this.errorMessage = 'Entrenamiento o usuario no encontrado.';
        } else if (err.status === 400) {
          this.errorMessage = 'Solicitud inválida. Verifica los datos.';
        } else {
          this.errorMessage = 'Error al marcar entrenamiento como realizado. Inténtalo de nuevo.';
        }
        setTimeout(() => this.errorMessage = null, 5000);
      }
    });
  }
}