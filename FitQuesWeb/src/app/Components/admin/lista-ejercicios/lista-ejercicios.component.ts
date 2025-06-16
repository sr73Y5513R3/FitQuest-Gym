import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { EjercicioCreateUpdateDto, GetEjercicioDto, Page } from '../../../models/ejercicio.model';
import { EjercicioService } from '../../../Services/ejercicio/ejercicio.service';
import { Router } from '@angular/router';
import { Nivel } from '../../../models/nivel.model';
import { NivelService } from '../../../Services/nivel/nivel.service';
import { MaterialService } from '../../../Services/material/material.service';
import { GetMaterialDto } from '../../../models/material.model';


@Component({
  selector: 'app-lista-ejercicios',
  templateUrl: './lista-ejercicios.component.html',
  styleUrl: './lista-ejercicios.component.css'
})
export class ListaEjerciciosComponent {

  materialesPage: Page<GetMaterialDto> | undefined;
  ejerciciosPage: Page<GetEjercicioDto> | undefined;
  currentPage: number = 0;
  pageSize: number = 6;
  searchTerm: string = '';

  create: boolean = false; 

  
  nombre: string = '';
  descripcion: string = '';
  series: number | null = null;
  repeticiones: number | null = null;
  duracion: number | null = null;
  urlImagen: string = '';


  niveles: Nivel[] = []; 
  selectedNivelId: number | null = null;
  editing: boolean = false; 

  
  errorMessage: string | null = null;
  successMessage: string | null = null;

  
  showAddMaterialToEjercicioForm: boolean = false; 
  ejercicioToAddMaterialToId: number | null = null;
  materialesDisponibles: GetMaterialDto[] = []; 
  materialesEnEjercicioActual: Set<number> = new Set();
  materialesEnEjercicioActualMap: Map<number, GetMaterialDto> = new Map();

  constructor(
    private ejercicioService: EjercicioService,
    private nivelService: NivelService,
    private router: Router,
    private materialService: MaterialService
  ) { }

  ngOnInit(): void {
    
    this.loadEjercicios();
    this.loadNiveles();
  }

  loadNiveles(): void {
    this.nivelService.findAll().subscribe({
      next: (data) => {
        this.niveles = data; 
        console.log('Niveles cargados:', this.niveles);
      },
      error: (err) => {
        console.error('Error al cargar niveles:', err);
        this.errorMessage = 'Hubo un error al cargar los niveles disponibles. Por favor, inténtalo de nuevo más tarde.';
      }
    });
  }


     createEjercicio(): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (!this.nombre || !this.descripcion || this.series === null || this.repeticiones === null ||
        this.duracion === null || !this.selectedNivelId) { 
      this.errorMessage = 'Por favor, completa todos los campos requeridos (nombre, descripción, series, repeticiones, duración, nivel).';
      return;
    }
    if (this.series < 1 || this.repeticiones < 1 || this.duracion < 0) {
      this.errorMessage = 'Series, repeticiones y duración deben ser números positivos.';
      return;
    }

   
    this.nivelService.findById(this.selectedNivelId).subscribe({
      next: (nivelSeleccionado: Nivel) => {
 
        const newEjercicio: EjercicioCreateUpdateDto = {
          nombre: this.nombre!,
          descripcion: this.descripcion!,
          series: this.series!,
          repeticiones: this.repeticiones!,
          duracion: this.duracion!,
          urlImagen: this.urlImagen || 'No hay',
          nivel: nivelSeleccionado, 
        };

        
        this.ejercicioService.createEjercicio(newEjercicio).subscribe({
      next: () => {
        this.successMessage = 'Ejercicio creado con éxito.';
       
        this.nombre = '';
        this.descripcion = '';
        this.series = null;
        this.repeticiones = null;
        this.duracion = null;
        this.urlImagen = '';
       

        this.loadEjercicios(); 

        setTimeout(() => {
          this.router.navigate(["/admin/ejercicio"])
          this.changeEstado()
          this.successMessage = null;
        }, 2000);
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error al crear ejercicio:', err);
        if (err.status === 403) {
          this.errorMessage = 'No tienes permisos para crear ejercicios.';
        } else {
          this.errorMessage = 'Hubo un error al crear el ejercicio. Inténtalo de nuevo.';
        }
      }

      
    });
      },
      error: (err) => {
        console.error('Error al obtener el nivel seleccionado:', err);
        this.errorMessage = 'No se pudo obtener la información del nivel seleccionado. Por favor, inténtalo de nuevo.';
      }
    });
  }

  goBack(): void {
    if (this.create) {
      this.changeEstado();
    } else {
      this.router.navigate(['/admin/ejercicios']);
    }
    this.errorMessage = null;
    this.successMessage = null;
  }

  loadEjercicios(): void {
    this.ejercicioService.findAllEjercicios(this.currentPage, this.pageSize, this.searchTerm)
      .subscribe({
        next: (data) => {
          this.ejerciciosPage = data;
          console.log('Ejercicios cargados:', this.ejerciciosPage);
        },
        error: (err) => {
          console.error('Error al cargar ejercicios:', err);
          this.errorMessage = 'Hubo un error al cargar los ejercicios.';
        }
      });
  }

  

  goToPage(page: number): void {
    this.currentPage = page;
    this.loadEjercicios();
  }

  nextPage(): void {
    if (this.ejerciciosPage && !this.ejerciciosPage.last) {
      this.currentPage++;
      this.loadEjercicios();
    }
  }

  previousPage(): void {
    if (this.ejerciciosPage && !this.ejerciciosPage.first) {
      this.currentPage--;
      this.loadEjercicios();
    }
  }

  onSearchChange(): void {
    this.currentPage = 0;
    this.loadEjercicios();
  }

  changeEstado(): void {
    this.create = !this.create;
  }

  deleteEntrenamiento(id: number | undefined): void {
    if (id === undefined) {
      this.errorMessage = 'ID de entrenamiento no válido para eliminar.';
      setTimeout(() => this.errorMessage = null, 3000);
      return;
    }

    if (confirm('¿Estás seguro de que quieres eliminar este entrenamiento?')) {
      this.ejercicioService.deleteEjercicio(id).subscribe({
        next: () => {
          this.successMessage = 'Ejercicio eliminado con éxito.';
          this.loadEjercicios();
          setTimeout(() => {
            this.successMessage = null;
          }, 3000);
        },
        error: (err: HttpErrorResponse) => {
          console.error('Error al eliminar entrenamiento:', err);
          if (err.status === 403) {
            this.errorMessage = 'No tienes permisos para eliminar ejercicios.';
          } else {
            this.errorMessage = 'Hubo un error al eliminar el ejercicio. Inténtalo de nuevo.';
          }
          setTimeout(() => {
            this.errorMessage = null;
          }, 5000);
        }
      });
    }
  }

  getMaterialesAsociadosList(): GetMaterialDto[] {
    return Array.from(this.materialesEnEjercicioActualMap.values());
  }

  addMaterialToEjercicio(materialId: number): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (this.ejercicioToAddMaterialToId === null) {
      this.errorMessage = 'No se ha seleccionado un entrenamiento para añadir el ejercicio.';
      return;
    }
    if (materialId === undefined || materialId === 0) { 
        this.errorMessage = 'ID de ejercicio no válido.';
        return;
    }

    this.ejercicioService.addMaterialToEjercicio(this.ejercicioToAddMaterialToId, materialId).subscribe({
      next: () => {
        this.successMessage = `Ejercicio añadido al entrenamiento con éxito.`;
        window.location.reload();
        this.showAddMaterialToEjercicioForm = false;
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

  loadAllMaterialesForSelection(): void {
    this.materialService.findAllMateriales(0, 500, '').subscribe({
      next: (data) => { 
        this.materialesDisponibles = data.content;
        console.log('Todos los materiales cargados para selección:', this.materialesDisponibles);
        this.filterAvailableMateriales(); 
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error al cargar todos los materiales:', err);
        this.errorMessage = 'No se pudieron cargar los materiales disponibles para selección. Inténtalo de nuevo.';
        setTimeout(() => this.errorMessage = null, 5000);
      }
    });
  }

  
  loadMaterialesFromEjercicio(ejercicioId: number): void {
    this.materialesEnEjercicioActual.clear(); // Limpia el Set

    this.ejercicioService.getEjercicioById(ejercicioId).subscribe({
      next: (ejercicio: GetEjercicioDto) => {
        if (ejercicio.materiales) {
          ejercicio.materiales.forEach(mat => {
            this.materialesEnEjercicioActual.add(mat.id!); // Añade solo el ID al Set
          });
          // ...
        }
      },
      // ...
    });
}


  filterAvailableMateriales(): void {

    if (this.materialesDisponibles.length > 0) { 
      this.materialesDisponibles = this.materialesDisponibles.filter(material =>
        !this.materialesEnEjercicioActual.has(material.id!)
      );
      console.log('Materiales disponibles después de filtrar:', this.materialesDisponibles);
    }
  }

  showAddMaterialToExerciseForm(ejercicioId: number): void {
    if (ejercicioId === undefined) {
      this.errorMessage = 'ID de ejercicio no válido.';
      return;
    }
    this.ejercicioToAddMaterialToId = ejercicioId; 
    this.showAddMaterialToEjercicioForm = true; 

    this.create = false;
    this.editing = false;

    this.errorMessage = null;
    this.successMessage = null;

    this.materialesDisponibles = []; 
    this.materialesEnEjercicioActual.clear(); 

    this.loadAllMaterialesForSelection();
    this.loadMaterialesFromEjercicio(ejercicioId);
  }

  changeAddMaterial() {
    this.showAddMaterialToEjercicioForm = false;
  }

  removeMaterialFromEjercicio(ejercicioId: number, materialId: number): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (ejercicioId === undefined || ejercicioId === 0 || materialId === undefined || materialId === 0) {
      this.errorMessage = 'IDs de ejercicio o material no válidos para la eliminación.';
      return;
    }

    if (!confirm('¿Estás seguro de que quieres eliminar este material de este ejercicio?')) {
      return; // El usuario canceló
    }

    this.ejercicioService.removeMaterialFromEjercicio(ejercicioId, materialId).subscribe({
      next: () => {
        this.successMessage = 'Material eliminado del ejercicio con éxito.';
        if (this.showAddMaterialToEjercicioForm && this.ejercicioToAddMaterialToId === ejercicioId) {
             this.loadMaterialesFromEjercicio(this.ejercicioToAddMaterialToId); 
             this.loadAllMaterialesForSelection();
        } 
        window.location.reload();
        setTimeout(() => this.successMessage = null, 2000);
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error al eliminar material del ejercicio:', err);
        if (err.status === 404) {
          this.errorMessage = 'Ejercicio o material no encontrado para eliminar.';
        } else {
          this.errorMessage = 'Hubo un error al eliminar el material del ejercicio. Inténtalo de nuevo.';
        }
        setTimeout(() => this.errorMessage = null, 5000);
      }
    });
  }
}
