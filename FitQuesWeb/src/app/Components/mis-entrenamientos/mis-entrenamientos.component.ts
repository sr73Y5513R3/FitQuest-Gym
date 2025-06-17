import { Component, OnInit } from '@angular/core';
import { GetEntrenoConEjercicioDto, EntrenamientoCreateUpdateDto, Page } from '../../models/entrenamiento.model';
import { GetEjercicioDto } from '../../models/ejercicio.model';
import { EntrenamientoService } from '../../Services/entrenamiento/entrenamiento.service';
import { EjercicioService } from '../../Services/ejercicio/ejercicio.service';
import { AuthService } from '../../Services/login/auth-service.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-mis-entrenamientos',
  templateUrl: './mis-entrenamientos.component.html',
  styleUrls: ['./mis-entrenamientos.component.css']
})

export class MisEntrenamientosComponent implements OnInit {

  entrenamientosPage: Page<GetEntrenoConEjercicioDto> | undefined;
  currentPage: number = 0;
  pageSize: number = 6;
  searchTerm: string = '';

  // Propiedades para el formulario de EDICIÓN
  showCreateEditForm: boolean = false; // Controla la visibilidad del formulario de edición
  editing: boolean = false; // Siempre será true cuando el formulario de edición esté visible
  currentEntrenamientoId: number | null = null;
  nombre: string = '';
  descripcion: string = '';
  calorias: number | null = null;
  puntos: number | null = null;

  errorMessage: string | null = null;
  successMessage: string | null = null;

  // Propiedades para la gestión de ejercicios
  showManageExercisesForm: boolean = false;
  entrenamientoIdToManageExercises: number | null = null;
  ejerciciosDisponibles: GetEjercicioDto[] = [];
  ejerciciosEnEntrenamientoActualMap: Map<number, GetEjercicioDto> = new Map();

  currentLoggedInEntrenadorId: string | null = null;

  constructor(
    private entrenamientoService: EntrenamientoService,
    private ejercicioService: EjercicioService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.currentLoggedInEntrenadorId = this.authService.getLoggedInUserId();

    if (this.currentLoggedInEntrenadorId) {
      this.loadMisEntrenamientos();
    } else {
      this.errorMessage = 'No se pudo identificar al entrenador. Por favor, inicia sesión de nuevo.';
      this.router.navigate(['/login']);
    }
  }

  loadMisEntrenamientos(): void {
    // Asegurarse de que el formulario de edición y gestión de ejercicios no estén visibles al cargar la lista principal
    this.showCreateEditForm = false;
    this.showManageExercisesForm = false;
    this.editing = false; // Resetear el estado de edición
    this.resetFormFields(); // Limpiar campos del formulario de edición

    if (!this.currentLoggedInEntrenadorId) {
      this.errorMessage = 'ID de entrenador no disponible para cargar entrenamientos.';
      return;
    }
    this.entrenamientoService.findAllEntrenamientosByEntrenador(
      this.currentLoggedInEntrenadorId,
      this.currentPage,
      this.pageSize,
      this.searchTerm
    ).subscribe({
      next: (data) => {
        this.entrenamientosPage = data;
        console.log('Mis entrenamientos cargados:', this.entrenamientosPage);
        this.errorMessage = null;
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error al cargar mis entrenamientos:', err);
        if (err.status === 403) {
          this.errorMessage = 'No tienes permisos para ver estos entrenamientos.';
        } else if (err.status === 404) {
          this.errorMessage = 'No se encontraron entrenamientos para este entrenador.';
        } else {
          this.errorMessage = 'Hubo un error al cargar tus entrenamientos.';
        }
        setTimeout(() => this.errorMessage = null, 5000);
      }
    });
  }

  // --- Lógica de Edición (dentro de MisEntrenamientosComponent) ---

  editEntrenamiento(entrenamiento: GetEntrenoConEjercicioDto): void {
    this.errorMessage = null;
    this.successMessage = null;

    this.editing = true;
    this.showCreateEditForm = true; // Mostrar el formulario de edición
    this.showManageExercisesForm = false; // Asegurarse de que la gestión de ejercicios no esté visible

    this.currentEntrenamientoId = entrenamiento.id!;
    this.nombre = entrenamiento.nombre;
    this.descripcion = entrenamiento.descripcion;
    this.calorias = entrenamiento.calorias || null;
    this.puntos = entrenamiento.puntos || null;
  }

  updateEntrenamiento(): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (!this.nombre || !this.descripcion || this.calorias === null || this.puntos === null) {
      this.errorMessage = 'Por favor, completa todos los campos requeridos (Nombre, Descripción, Calorías, Puntos).';
      setTimeout(() => this.errorMessage = null, 5000);
      return;
    }
    if (this.calorias <= 0 || this.puntos <= 0) {
      this.errorMessage = 'Calorías y Puntos deben ser números positivos.';
      setTimeout(() => this.errorMessage = null, 5000);
      return;
    }
    if (!this.currentLoggedInEntrenadorId) {
      this.errorMessage = 'Error: ID del entrenador no disponible. Por favor, vuelve a iniciar sesión.';
      setTimeout(() => this.errorMessage = null, 5000);
      return;
    }
    if (this.currentEntrenamientoId === null) {
      this.errorMessage = 'Error: No se puede actualizar un entrenamiento sin ID.';
      setTimeout(() => this.errorMessage = null, 5000);
      return;
    }

    const entrenamientoData: EntrenamientoCreateUpdateDto = {
      nombre: this.nombre,
      descripcion: this.descripcion,
      calorias: this.calorias,
      puntos: this.puntos,
      entrenadorId: this.currentLoggedInEntrenadorId!
    };

    this.entrenamientoService.updateEntrenamiento(this.currentEntrenamientoId, entrenamientoData).subscribe({
      next: () => {
        this.successMessage = 'Entrenamiento actualizado con éxito.';
        this.loadMisEntrenamientos(); // Recargar la lista después de actualizar
        setTimeout(() => this.successMessage = null, 2000);
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error al actualizar entrenamiento:', err);
        if (err.status === 403) {
          this.errorMessage = 'No tienes permisos para actualizar este entrenamiento.';
        } else if (err.status === 404) {
          this.errorMessage = 'Entrenamiento no encontrado.';
        } else if (err.status === 401) {
          this.errorMessage = 'No autorizado. Por favor, inicia sesión de nuevo.';
          this.router.navigate(['/login']);
        } else {
          this.errorMessage = 'Hubo un error al actualizar el entrenamiento. Inténtalo de nuevo.';
        }
        setTimeout(() => this.errorMessage = null, 5000);
      }
    });
  }

  cancelForm(): void {
    this.showCreateEditForm = false; // Ocultar el formulario
    this.editing = false;
    this.resetFormFields();
    this.errorMessage = null;
    this.successMessage = null;
    this.loadMisEntrenamientos(); // Opcional: recargar la lista si quieres refrescar algo
  }

  private resetFormFields(): void {
    this.currentEntrenamientoId = null;
    this.nombre = '';
    this.descripcion = '';
    this.calorias = null;
    this.puntos = null;
  }

  // --- Lógica de la lista y paginación ---

  deleteEntrenamiento(id: number | undefined): void {
    if (id === undefined) {
      this.errorMessage = 'ID de entrenamiento no válido para eliminar.';
      setTimeout(() => this.errorMessage = null, 3000);
      return;
    }

    if (confirm('¿Estás seguro de que quieres eliminar este entrenamiento? Esta acción es irreversible.')) {
      this.entrenamientoService.deleteEntrenamiento(id).subscribe({
        next: () => {
          this.successMessage = 'Entrenamiento eliminado con éxito.';
          this.loadMisEntrenamientos();
          setTimeout(() => this.successMessage = null, 3000);
        },
        error: (err: HttpErrorResponse) => {
          console.error('Error al eliminar entrenamiento:', err);
          if (err.status === 403) {
            this.errorMessage = 'No tienes permisos para eliminar este entrenamiento.';
          } else if (err.status === 404) {
            this.errorMessage = 'Entrenamiento no encontrado.';
          } else {
            this.errorMessage = 'Hubo un error al eliminar el entrenamiento. Inténtalo de nuevo.';
          }
          setTimeout(() => this.errorMessage = null, 5000);
        }
      });
    }
  }

  goToPage(page: number): void {
    this.currentPage = page;
    this.loadMisEntrenamientos();
  }

  nextPage(): void {
    if (this.entrenamientosPage && !this.entrenamientosPage.last) {
      this.currentPage++;
      this.loadMisEntrenamientos();
    }
  }

  previousPage(): void {
    if (this.entrenamientosPage && !this.entrenamientosPage.first) {
      this.currentPage--;
      this.loadMisEntrenamientos();
    }
  }

  onSearchChange(): void {
    this.currentPage = 0;
    this.loadMisEntrenamientos();
  }

  // --- Lógica de Gestión de Ejercicios ---

  showManageExercises(entrenamientoId: number | undefined): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (entrenamientoId === undefined) {
      this.errorMessage = 'ID de entrenamiento no válido para gestionar ejercicios.';
      return;
    }
    this.entrenamientoIdToManageExercises = entrenamientoId;
    this.showManageExercisesForm = true;
    this.showCreateEditForm = false; // Asegurarse de que el formulario de edición no esté visible

    this.ejerciciosDisponibles = [];
    this.ejerciciosEnEntrenamientoActualMap.clear();

    this.loadAllEjerciciosForSelection();
    this.loadEjerciciosFromEntrenamiento(entrenamientoId);
  }

  loadAllEjerciciosForSelection(): void {
    this.ejercicioService.findAllEjercicios(0, 500, '').subscribe({
      next: (data) => {
        this.ejerciciosDisponibles = data.content;
        console.log('Todos los ejercicios cargados para selección:', this.ejerciciosDisponibles);
        this.filterAvailableEjercicios();
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error al cargar todos los ejercicios:', err);
        this.errorMessage = 'No se pudieron cargar los ejercicios disponibles para selección. Inténtalo de nuevo.';
        setTimeout(() => this.errorMessage = null, 5000);
      }
    });
  }

  loadEjerciciosFromEntrenamiento(entrenamientoId: number): void {
    this.ejerciciosEnEntrenamientoActualMap.clear();

    this.entrenamientoService.getEntrenamientoById(entrenamientoId).subscribe({
      next: (entrenamiento: GetEntrenoConEjercicioDto) => {
        if (entrenamiento.ejercicios) {
          entrenamiento.ejercicios.forEach(ej => {
            this.ejerciciosEnEntrenamientoActualMap.set(ej.id!, ej);
          });
          console.log('Ejercicios YA en el entrenamiento (Map):', this.ejerciciosEnEntrenamientoActualMap);
          this.filterAvailableEjercicios();
        } else {
          console.log('El entrenamiento no tiene la propiedad "ejercicios" o está vacía.');
          this.ejerciciosEnEntrenamientoActualMap.clear();
          this.filterAvailableEjercicios();
        }
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error al cargar ejercicios del entrenamiento:', err);
        this.errorMessage = 'No se pudieron cargar los ejercicios asociados al entrenamiento. Inténtalo de nuevo.';
        setTimeout(() => this.errorMessage = null, 5000);
      }
    });
  }

  filterAvailableEjercicios(): void {
    if (!this.ejerciciosDisponibles || this.ejerciciosDisponibles.length === 0) {
      return;
    }
    this.ejerciciosDisponibles = this.ejerciciosDisponibles.filter(ejercicio =>
      !this.ejerciciosEnEntrenamientoActualMap.has(ejercicio.id!)
    );
    console.log('Ejercicios disponibles después de filtrar:', this.ejerciciosDisponibles);
  }

  addEjercicioToEntrenamiento(ejercicioId: number): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (this.entrenamientoIdToManageExercises === null) {
      this.errorMessage = 'No se ha seleccionado un entrenamiento para añadir el ejercicio.';
      return;
    }
    if (ejercicioId === undefined || ejercicioId === 0) {
      this.errorMessage = 'ID de ejercicio no válido.';
      return;
    }

    this.entrenamientoService.addEjercicioToEntrenamiento(this.entrenamientoIdToManageExercises, ejercicioId).subscribe({
      next: () => {
        this.successMessage = `Ejercicio añadido al entrenamiento con éxito.`;
        this.loadEjerciciosFromEntrenamiento(this.entrenamientoIdToManageExercises!);
        this.loadAllEjerciciosForSelection();
        setTimeout(() => this.successMessage = null, 2000);
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error al añadir ejercicio al entrenamiento:', err);
        if (err.status === 409) {
          this.errorMessage = 'El ejercicio ya está asociado a este entrenamiento o hay un conflicto.';
        } else if (err.status === 404) {
          this.errorMessage = 'Entrenamiento o ejercicio no encontrado.';
        } else {
          this.errorMessage = 'Hubo un error al añadir el ejercicio al entrenamiento. Inténtalo de nuevo.';
        }
        setTimeout(() => this.errorMessage = null, 5000);
      }
    });
  }

  removeEjercicioFromEntrenamiento(entrenamientoId: number, ejercicioId: number): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (entrenamientoId === undefined || entrenamientoId === 0 || ejercicioId === undefined || ejercicioId === 0) {
      this.errorMessage = 'IDs de entrenamiento o ejercicio no válidos para la eliminación.';
      return;
    }

    if (!confirm('¿Estás seguro de que quieres eliminar este ejercicio de este entrenamiento?')) {
      return;
    }

    this.entrenamientoService.deleteEjercicioToEntrenamiento(entrenamientoId, ejercicioId).subscribe({
      next: () => {
        this.successMessage = 'Ejercicio eliminado del entrenamiento con éxito.';
        this.loadEjerciciosFromEntrenamiento(entrenamientoId);
        this.loadAllEjerciciosForSelection();
        setTimeout(() => this.successMessage = null, 2000);
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error al eliminar ejercicio del entrenamiento:', err);
        if (err.status === 404) {
          this.errorMessage = 'Entrenamiento o ejercicio no encontrado para eliminar.';
        } else {
          this.errorMessage = 'Hubo un error al eliminar el ejercicio del entrenamiento. Inténtalo de nuevo.';
        }
        setTimeout(() => this.errorMessage = null, 5000);
      }
    });
  }

  goBackToList(): void {
    this.showManageExercisesForm = false;
    this.entrenamientoIdToManageExercises = null;
    this.ejerciciosDisponibles = [];
    this.ejerciciosEnEntrenamientoActualMap.clear();
    this.loadMisEntrenamientos();
    this.errorMessage = null;
    this.successMessage = null;
  }

  getEjerciciosAsociadosList(): GetEjercicioDto[] {
    return Array.from(this.ejerciciosEnEntrenamientoActualMap.values());
  }
}