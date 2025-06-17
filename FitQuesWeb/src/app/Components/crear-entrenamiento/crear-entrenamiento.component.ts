import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { EntrenamientoCreateUpdateDto, GetEntrenoConEjercicioDto } from '../../models/entrenamiento.model';
import { GetEjercicioDto } from '../../models/ejercicio.model'; // Asegúrate de importar GetEjercicioDto
import { EntrenamientoService } from '../../Services/entrenamiento/entrenamiento.service';
import { EjercicioService } from '../../Services/ejercicio/ejercicio.service'; // Asegúrate de importar EjercicioService
import { AuthService } from '../../Services/login/auth-service.service';

@Component({
  selector: 'app-crear-entrenamiento',
  templateUrl: './crear-entrenamiento.component.html',
  styleUrls: ['./crear-entrenamiento.component.css']
})
export class CrearEntrenamientoComponent implements OnInit {

  nombre: string = '';
  descripcion: string = '';
  calorias: number | null = null;
  puntos: number | null = null;

  errorMessage: string | null = null;
  successMessage: string | null = null;

  currentLoggedInEntrenadorId: string | null = null;

  // NUEVAS PROPIEDADES para la selección de ejercicios
  showExerciseSelection: boolean = false;
  newlyCreatedEntrenamientoId: number | null = null;
  ejerciciosDisponibles: GetEjercicioDto[] = [];
  ejerciciosEnEntrenamientoActualMap: Map<number, GetEjercicioDto> = new Map();

  constructor(
    private entrenamientoService: EntrenamientoService,
    private ejercicioService: EjercicioService, // Inyectar EjercicioService
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.currentLoggedInEntrenadorId = this.authService.getLoggedInUserId();
    // Asegurarse de que el formulario de creación esté visible al iniciar
    this.showExerciseSelection = false;
    this.resetForm();
  }

  createEntrenamiento(): void {
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

    const entrenamientoData: EntrenamientoCreateUpdateDto = {
      nombre: this.nombre,
      descripcion: this.descripcion,
      calorias: this.calorias,
      puntos: this.puntos,
      entrenadorId: this.currentLoggedInEntrenadorId!
    };

    this.entrenamientoService.createEntrenamiento(entrenamientoData).subscribe({
      next: (newEntrenamiento: GetEntrenoConEjercicioDto) => { // AQUI CAPTURAMOS EL OBJETO CREADO
        this.successMessage = 'Entrenamiento creado con éxito. Ahora puedes añadir ejercicios.';
        this.newlyCreatedEntrenamientoId = newEntrenamiento.id!; // Guardar el ID del entrenamiento recién creado

        // Mover a la sección de añadir ejercicios
        this.showExerciseSelection = true;
        this.resetForm(); // Limpiar el formulario de creación por si se vuelve a usar

        // Cargar ejercicios para la selección
        this.loadAllEjerciciosForSelection();
        this.loadEjerciciosFromEntrenamiento(this.newlyCreatedEntrenamientoId); // Carga los ejercicios que YA tiene (ninguno al principio)
        setTimeout(() => this.successMessage = null, 2000);
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error al crear entrenamiento:', err);
        if (err.status === 403) {
          this.errorMessage = 'No tienes permisos para crear entrenamientos.';
        } else if (err.status === 401) {
          this.errorMessage = 'No autorizado. Por favor, inicia sesión de nuevo.';
          this.router.navigate(['/login']);
        } else {
          this.errorMessage = 'Hubo un error al crear el entrenamiento. Inténtalo de nuevo.';
        }
        setTimeout(() => this.errorMessage = null, 5000);
      }
    });
  }

  // Método para volver de la sección de creación (no desde la de ejercicios)
  cancelForm(): void {
    this.router.navigate(['/mis-entrenamientos']);
  }

  // NUEVOS MÉTODOS para la gestión de ejercicios, copiados de MisEntrenamientosComponent
  loadAllEjerciciosForSelection(): void {
    this.ejercicioService.findAllEjercicios(0, 500, '').subscribe({
      next: (data) => {
        this.ejerciciosDisponibles = data.content;
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
          this.filterAvailableEjercicios();
        } else {
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
  }

  addEjercicioToEntrenamiento(ejercicioId: number): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (this.newlyCreatedEntrenamientoId === null) {
      this.errorMessage = 'No se ha seleccionado un entrenamiento para añadir el ejercicio.';
      return;
    }
    if (ejercicioId === undefined || ejercicioId === 0) {
      this.errorMessage = 'ID de ejercicio no válido.';
      return;
    }

    this.entrenamientoService.addEjercicioToEntrenamiento(this.newlyCreatedEntrenamientoId, ejercicioId).subscribe({
      next: () => {
        this.successMessage = `Ejercicio añadido al entrenamiento con éxito.`;
        this.loadEjerciciosFromEntrenamiento(this.newlyCreatedEntrenamientoId!);
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

  getEjerciciosAsociadosList(): GetEjercicioDto[] {
    return Array.from(this.ejerciciosEnEntrenamientoActualMap.values());
  }

  // Método para finalizar la adición de ejercicios y volver a la lista principal
  finishExerciseSelection(): void {
    this.showExerciseSelection = false;
    this.newlyCreatedEntrenamientoId = null;
    this.ejerciciosDisponibles = [];
    this.ejerciciosEnEntrenamientoActualMap.clear();
    this.errorMessage = null;
    this.successMessage = null;
    this.router.navigate(['entrenador/mis-entrenamientos']); // Redirigir a la lista de entrenamientos
  }

  private resetForm(): void {
    this.nombre = '';
    this.descripcion = '';
    this.calorias = null;
    this.puntos = null;
    this.errorMessage = null;
    this.successMessage = null;
  }
}