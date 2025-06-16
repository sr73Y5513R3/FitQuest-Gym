import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { EntrenamientoCreateUpdateDto, GetEntrenoConEjercicioDto, Page } from '../../../models/entrenamiento.model';
import { UsuarioService } from '../../../Services/usuario/usuario.service';
import { EntrenamientoService } from '../../../Services/entrenamiento/entrenamiento.service';
import { Router } from '@angular/router';
import { Entrenador } from '../../../models/usuario.model';
import { GetEjercicioDto } from '../../../models/ejercicio.model';
import { EjercicioService } from '../../../Services/ejercicio/ejercicio.service';

@Component({
  selector: 'app-lista-entrenamientos',
  templateUrl: './lista-entrenamientos.component.html',
  styleUrl: './lista-entrenamientos.component.css'
})
export class ListaEntrenamientosComponent implements OnInit{



  entrenamientosPage: Page<GetEntrenoConEjercicioDto> | undefined;
  currentPage: number = 0;
  pageSize: number = 6;
  searchTerm: string = '';

  create: boolean = false;

  nombre: string = '';
  descripcion: string = '';
  calorias: number | null = null;
  puntos: number | null = null;
  entrenadorId: string = ''; 

  entrenadores: Entrenador[] = []; 
  errorMessage: string | null = null;
  successMessage: string | null = null;
  editing: boolean = false; 
  currentEntrenamientoId: number | null = null;

  showAddEjercicioForm: boolean = false; 
  entrenamientoToAddExerciseToId: number | null = null; 
  ejercicioToAddId: number = 0; 
  ejerciciosDisponibles: GetEjercicioDto[] = []; 
  ejerciciosPage: Page<GetEjercicioDto> | undefined;
  ejerciciosEnEntrenamientoActual: Set<number> = new Set();

  constructor(
    private entrenamientoService: EntrenamientoService,
    private usuarioService: UsuarioService,
    private router: Router,
    private ejercicioService: EjercicioService
  ) { }

  ngOnInit(): void {
    this.loadEntrenadores();
    this.loadEntrenamientos(); 
  }

  loadEntrenadores(): void {
    this.usuarioService.findAllEntrenadores(0, 100).subscribe({ // Ajusta page y size según necesites cargar todos
      next: (pageOfEntrenadores) => {
        this.entrenadores = pageOfEntrenadores.content; // Extrae el array de entrenadores del objeto Page
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error al cargar entrenadores:', err);
        this.errorMessage = 'No se pudieron cargar los entrenadores. Inténtalo de nuevo.';
      }
    });
  }

  createEntrenamiento(): void {
    this.errorMessage = null;
    this.successMessage = null; 

    if (!this.nombre || !this.descripcion || this.calorias === null || this.puntos === null || !this.entrenadorId) {
      this.errorMessage = 'Por favor, completa todos los campos requeridos.';
      return;
    }
    if (this.calorias < 0 || this.puntos < 0) {
      this.errorMessage = 'Calorías y puntos no pueden ser negativos.';
      return;
    }

    const newEntrenamiento: EntrenamientoCreateUpdateDto = {
      nombre: this.nombre,
      descripcion: this.descripcion,
      calorias: this.calorias,
      puntos: this.puntos,
      entrenadorId: this.entrenadorId
    };

    this.entrenamientoService.createEntrenamiento(newEntrenamiento).subscribe({
      next: () => {
        this.successMessage = 'Entrenamiento creado con éxito.';
        this.nombre = '';
        this.descripcion = '';
        this.calorias = null;
        this.puntos = null;
        this.entrenadorId = '';

        setTimeout(() => {
          window.location.reload();
          this.create = false;
        }, 2000);
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error al crear entrenamiento:', err);
        if (err.status === 403) {
          this.errorMessage = 'No tienes permisos para crear entrenamientos.';
        } else {
          this.errorMessage = 'Hubo un error al crear el entrenamiento. Inténtalo de nuevo.';
        }
      }
    });

  }

   updateEntrenamiento(): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (this.currentEntrenamientoId === null) {
      this.errorMessage = 'No se ha seleccionado ningún entrenamiento para actualizar.';
      return;
    }

    if (!this.nombre || !this.descripcion || this.calorias === null || this.puntos === null || !this.entrenadorId) {
      this.errorMessage = 'Por favor, completa todos los campos requeridos.';
      return;
    }
    if (this.calorias < 0 || this.puntos < 0) {
      this.errorMessage = 'Calorías y puntos no pueden ser negativos.';
      return;
    }

    const updatedEntrenamiento: EntrenamientoCreateUpdateDto = {
      nombre: this.nombre,
      descripcion: this.descripcion,
      calorias: this.calorias,
      puntos: this.puntos,
      entrenadorId: this.entrenadorId
    };

    this.entrenamientoService.updateEntrenamiento(this.currentEntrenamientoId, updatedEntrenamiento).subscribe({
      next: () => {
        this.successMessage = 'Entrenamiento actualizado con éxito.';
        window.location.reload();
        this.create = false;
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error al actualizar entrenamiento:', err);
        if (err.status === 403) {
          this.errorMessage = 'No tienes permisos para actualizar entrenamientos.';
        } else {
          this.errorMessage = 'Hubo un error al actualizar el entrenamiento. Inténtalo de nuevo.';
        }
        setTimeout(() => this.errorMessage = null, 5000); // Limpia el mensaje de error después de un tiempo
      }
    });

    
  }


  
  editEntrenamiento(entrenamiento: GetEntrenoConEjercicioDto): void {
    this.create = true; 
    this.editing = true; 
    this.currentEntrenamientoId = entrenamiento.id!; 

    
    this.nombre = entrenamiento.nombre;
    this.descripcion = entrenamiento.descripcion;
    this.calorias = entrenamiento.calorias;
    this.puntos = entrenamiento.puntos;
    this.entrenadorId = entrenamiento.entrenador.id;
    this.errorMessage = null;
    this.successMessage = null;
  }

  deleteEntrenamiento(id: number | undefined): void {
    if (id === undefined) {
      this.errorMessage = 'ID de entrenamiento no válido para eliminar.';
      setTimeout(() => this.errorMessage = null, 3000);
      return;
    }

    if (confirm('¿Estás seguro de que quieres eliminar este entrenamiento?')) {
      this.entrenamientoService.deleteEntrenamiento(id).subscribe({
        next: () => {
          this.successMessage = 'Entrenamiento eliminado con éxito.';
          this.loadEntrenamientos(); // Recargar la lista de entrenamientos
          setTimeout(() => {
            this.successMessage = null;
          }, 3000);
        },
        error: (err: HttpErrorResponse) => {
          console.error('Error al eliminar entrenamiento:', err);
          if (err.status === 403) {
            this.errorMessage = 'No tienes permisos para eliminar entrenamientos.';
          } else {
            this.errorMessage = 'Hubo un error al eliminar el entrenamiento. Inténtalo de nuevo.';
          }
          setTimeout(() => {
            this.errorMessage = null;
          }, 5000);
        }
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/admin/entrenamiento']);
  }

  loadEntrenamientos(): void {
    this.entrenamientoService.findAllEntrenamientos(this.currentPage, this.pageSize,this.searchTerm)
      .subscribe({
        next: (data) => {
          this.entrenamientosPage = data;
          console.log('Entrenamientos cargados:', this.entrenamientosPage);
        },
        error: (err) => {
          console.error('Error al cargar entrenamientos:', err);
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

  changeEstado() {
    this.create = !this.create;
  }

  showAddExerciseForm(entrenamientoId: number | undefined): void {
    if (entrenamientoId === undefined) {
      this.errorMessage = 'ID de entrenamiento no válido.';
      return;
    }
    this.entrenamientoToAddExerciseToId = entrenamientoId;
    this.showAddEjercicioForm = true;
    this.create = false; 
    this.editing = false;
    this.currentEntrenamientoId = null;
    this.errorMessage = null;
    this.successMessage = null;

    this.ejerciciosDisponibles = []; 
    this.loadAllEjerciciosForSelection(); 
    this.loadEjerciciosFromEntrenamiento(entrenamientoId);
  }


  addEjercicioToEntrenamiento(ejercicioId: number): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (this.entrenamientoToAddExerciseToId === null) {
      this.errorMessage = 'No se ha seleccionado un entrenamiento para añadir el ejercicio.';
      return;
    }
    if (ejercicioId === undefined || ejercicioId === 0) { 
        this.errorMessage = 'ID de ejercicio no válido.';
        return;
    }

    this.entrenamientoService.addEjercicioToEntrenamiento(this.entrenamientoToAddExerciseToId, ejercicioId).subscribe({
      next: () => {
        this.successMessage = `Ejercicio añadido al entrenamiento con éxito.`;
        this.loadEntrenamientos();
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
    this.ejerciciosEnEntrenamientoActual.clear(); 


    this.entrenamientoService.getEntrenamientoById(entrenamientoId).subscribe({
      next: (entrenamiento: GetEntrenoConEjercicioDto) => {
        if (entrenamiento.ejercicios) {
          entrenamiento.ejercicios.forEach(ej => {
            this.ejerciciosEnEntrenamientoActual.add(ej.id!); 
          });
          console.log('IDs de ejercicios ya en el entrenamiento:', this.ejerciciosEnEntrenamientoActual);
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
    if (this.ejerciciosDisponibles.length > 0 && this.ejerciciosEnEntrenamientoActual.size > 0) {
      this.ejerciciosDisponibles = this.ejerciciosDisponibles.filter(ejercicio =>
        !this.ejerciciosEnEntrenamientoActual.has(ejercicio.id!)
      );
      console.log('Ejercicios disponibles después de filtrar:', this.ejerciciosDisponibles);
    }
  }
  
  changeAddExercise() {
    this.showAddEjercicioForm = false;
  }
}
