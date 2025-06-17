import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
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
export class ListaEjerciciosComponent implements OnInit {

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

  errorMessage: string | null = null;
  successMessage: string | null = null;

  showAddMaterialToEjercicioForm: boolean = false;
  ejercicioToAddMaterialToId: number | null = null;
  materialesDisponibles: GetMaterialDto[] = [];
  materialesEnEjercicioActual: Set<number> = new Set();
  materialesEnEjercicioActualMap: Map<number, GetMaterialDto> = new Map();

  editingEjercicio: boolean = false;
  currentEjercicioId: number | null = null;

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
        setTimeout(() => this.errorMessage = null, 5000);
      }
    });
  }

  changeEstado(): void {
    this.create = true;
    this.editingEjercicio = false;
    this.showAddMaterialToEjercicioForm = false;
    this.currentEjercicioId = null;

    this.nombre = '';
    this.descripcion = '';
    this.series = null;
    this.repeticiones = null;
    this.duracion = null;
    this.urlImagen = '';
    this.selectedNivelId = null;

    this.errorMessage = null;
    this.successMessage = null;
  }

  createEjercicio(): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (!this.nombre || !this.descripcion || this.series === null || this.repeticiones === null ||
      this.duracion === null || !this.selectedNivelId) {
      this.errorMessage = 'Por favor, completa todos los campos requeridos (nombre, descripción, series, repeticiones, duración, nivel).';
      setTimeout(() => this.errorMessage = null, 5000);
      return;
    }
    if (this.series < 1 || this.repeticiones < 1 || this.duracion < 0) {
      this.errorMessage = 'Series, repeticiones y duración deben ser números positivos.';
      setTimeout(() => this.errorMessage = null, 5000);
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
            this.selectedNivelId = null;

            this.loadEjercicios();
            this.create = false;

            setTimeout(() => {
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
            setTimeout(() => this.errorMessage = null, 5000);
          }
        });
      },
      error: (err) => {
        console.error('Error al obtener el nivel seleccionado:', err);
        this.errorMessage = 'No se pudo obtener la información del nivel seleccionado. Por favor, inténtalo de nuevo.';
        setTimeout(() => this.errorMessage = null, 5000);
      }
    });
  }

  goBack(): void {
    this.create = false;
    this.editingEjercicio = false;
    this.showAddMaterialToEjercicioForm = false;
    this.currentEjercicioId = null;
    this.ejercicioToAddMaterialToId = null;

    this.errorMessage = null;
    this.successMessage = null;

    this.loadEjercicios();
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
          setTimeout(() => this.errorMessage = null, 5000);
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
      this.errorMessage = 'No se ha seleccionado un EJERCICIO para añadir el material.';
      return;
    }
    if (materialId === undefined || materialId === 0) {
        this.errorMessage = 'ID de MATERIAL no válido.';
        return;
    }

    this.ejercicioService.addMaterialToEjercicio(this.ejercicioToAddMaterialToId, materialId).subscribe({
      next: () => {
        this.successMessage = `Material añadido al ejercicio con éxito.`;
        this.loadMaterialesFromEjercicio(this.ejercicioToAddMaterialToId!);
        this.loadAllMaterialesForSelection();
        setTimeout(() => this.successMessage = null, 2000);
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error al añadir material al ejercicio:', err);
        if (err.status === 409) {
          this.errorMessage = 'El material ya está asociado a este ejercicio o hay un conflicto.';
        } else if (err.status === 404) {
          this.errorMessage = 'Ejercicio o material no encontrado.';
        } else {
          this.errorMessage = 'Hubo un error al añadir el material al ejercicio. Inténtalo de nuevo.';
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
    this.materialesEnEjercicioActualMap.clear();

    this.ejercicioService.getEjercicioById(ejercicioId).subscribe({
      next: (ejercicio: GetEjercicioDto) => {
        if (ejercicio.materiales) {
          ejercicio.materiales.forEach(mat => {
            this.materialesEnEjercicioActualMap.set(mat.id!, mat);
          });
          console.log('Materiales YA en el ejercicio (Map):', this.materialesEnEjercicioActualMap);
          this.filterAvailableMateriales();
        } else {
          console.log('El ejercicio no tiene la propiedad "materiales" o está vacía.');
          this.materialesEnEjercicioActualMap.clear();
          this.filterAvailableMateriales();
        }
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error al cargar materiales del ejercicio:', err);
        this.errorMessage = 'No se pudieron cargar los materiales asociados al ejercicio. Inténtalo de nuevo.';
        setTimeout(() => this.errorMessage = null, 5000);
      }
    });
  }

  filterAvailableMateriales(): void {
    if (this.materialesDisponibles.length === 0) {
      console.log('No hay materiales disponibles para filtrar (lista vacía).');
      return;
    }

    this.materialesDisponibles = this.materialesDisponibles.filter(material =>
      !this.materialesEnEjercicioActualMap.has(material.id!)
    );
    console.log('Materiales disponibles DESPUÉS de filtrar:', this.materialesDisponibles);
  }

  showAddMaterialToExerciseForm(ejercicioId: number): void {
    if (ejercicioId === undefined) {
      this.errorMessage = 'ID de ejercicio no válido.';
      setTimeout(() => this.errorMessage = null, 5000);
      return;
    }
    this.ejercicioToAddMaterialToId = ejercicioId;
    this.showAddMaterialToEjercicioForm = true;

    this.create = false;
    this.editingEjercicio = false;

    this.errorMessage = null;
    this.successMessage = null;

    this.materialesDisponibles = [];
    this.materialesEnEjercicioActualMap.clear();

    this.loadAllMaterialesForSelection();
    this.loadMaterialesFromEjercicio(ejercicioId);
  }

  removeMaterialFromEjercicio(ejercicioId: number, materialId: number): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (ejercicioId === undefined || ejercicioId === 0 || materialId === undefined || materialId === 0) {
      this.errorMessage = 'IDs de ejercicio o material no válidos para la eliminación.';
      setTimeout(() => this.errorMessage = null, 5000);
      return;
    }

    if (!confirm('¿Estás seguro de que quieres eliminar este material de este ejercicio?')) {
      return;
    }

    this.ejercicioService.removeMaterialFromEjercicio(ejercicioId, materialId).subscribe({
      next: () => {
        this.successMessage = 'Material eliminado del ejercicio con éxito.';
        this.loadMaterialesFromEjercicio(ejercicioId);
        this.loadAllMaterialesForSelection();
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

  changeAddMaterial(): void {
    this.showAddMaterialToEjercicioForm = false;
    this.ejercicioToAddMaterialToId = null;
    this.loadEjercicios();
  }

  editEjercicio(ejercicio: GetEjercicioDto): void {
    console.log('Iniciando edición de ejercicio:', ejercicio);

    this.editingEjercicio = true;
    this.create = true;
    this.showAddMaterialToEjercicioForm = false;

    this.currentEjercicioId = ejercicio.id!;

    this.nombre = ejercicio.nombre;
    this.descripcion = ejercicio.descripcion;
    this.series = ejercicio.series !== undefined ? ejercicio.series : null;
    this.repeticiones = ejercicio.repeticiones !== undefined ? ejercicio.repeticiones : null;
    this.duracion = ejercicio.duracion !== undefined ? ejercicio.duracion : null;
    this.urlImagen = ejercicio.urlImagen || '';
    this.selectedNivelId = ejercicio.nivel?.id || null;

    this.errorMessage = null;
    this.successMessage = null;

    console.log('Estado después de editEjercicio:', {
      editingEjercicio: this.editingEjercicio,
      create: this.create,
      showAddMaterialToEjercicioForm: this.showAddMaterialToEjercicioForm,
      currentEjercicioId: this.currentEjercicioId,
      nombre: this.nombre
    });
  }

  updateEjercicio(): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (this.currentEjercicioId === null) {
      this.errorMessage = 'No se ha seleccionado ningún ejercicio para actualizar.';
      setTimeout(() => this.errorMessage = null, 5000);
      return;
    }

    if (!this.nombre || !this.descripcion || this.series === null || this.repeticiones === null ||
      this.duracion === null || !this.selectedNivelId) {
      this.errorMessage = 'Por favor, completa todos los campos requeridos (nombre, descripción, series, repeticiones, duración, nivel).';
      setTimeout(() => this.errorMessage = null, 5000);
      return;
    }
    if (this.series < 1 || this.repeticiones < 1 || this.duracion < 0) {
      this.errorMessage = 'Series, repeticiones y duración deben ser números positivos.';
      setTimeout(() => this.errorMessage = null, 5000);
      return;
    }

    this.nivelService.findById(this.selectedNivelId).subscribe({
      next: (nivelSeleccionado: Nivel) => {
        const updatedEjercicio: EjercicioCreateUpdateDto = {
          nombre: this.nombre,
          descripcion: this.descripcion,
          series: this.series!,
          repeticiones: this.repeticiones!,
          duracion: this.duracion!,
          urlImagen: this.urlImagen || 'No hay',
          nivel: nivelSeleccionado
        };

        this.ejercicioService.updateEjercicio(this.currentEjercicioId!, updatedEjercicio).subscribe({
          next: () => {
            this.successMessage = 'Ejercicio actualizado con éxito.';
            this.loadEjercicios();

            this.editingEjercicio = false;
            this.create = false;
            this.currentEjercicioId = null;

            this.nombre = '';
            this.descripcion = '';
            this.series = null;
            this.repeticiones = null;
            this.duracion = null;
            this.urlImagen = '';
            this.selectedNivelId = null;

            setTimeout(() => {
              this.successMessage = null;
            }, 2000);
          },
          error: (err: HttpErrorResponse) => {
            console.error('Error al actualizar ejercicio:', err);
            if (err.status === 403) {
              this.errorMessage = 'No tienes permisos para actualizar ejercicios.';
            } else if (err.status === 404) {
              this.errorMessage = 'Ejercicio no encontrado.';
            } else {
              this.errorMessage = 'Hubo un error al actualizar el ejercicio. Inténtalo de nuevo.';
            }
            setTimeout(() => this.errorMessage = null, 5000);
          }
        });
      },
      error: (err) => {
        console.error('Error al obtener el nivel seleccionado para actualizar:', err);
        this.errorMessage = 'No se pudo obtener la información del nivel seleccionado. Por favor, inténtalo de nuevo.';
        setTimeout(() => this.errorMessage = null, 5000);
      }
    });
  }

  cancelEditEjercicio(): void {
    console.log('Cancelando edición de ejercicio.');

    this.editingEjercicio = false;
    this.create = false;
    this.currentEjercicioId = null;

    this.nombre = '';
    this.descripcion = '';
    this.series = null;
    this.repeticiones = null;
    this.duracion = null;
    this.urlImagen = '';
    this.selectedNivelId = null;

    this.errorMessage = null;
    this.successMessage = null;

    this.loadEjercicios();
  }
}