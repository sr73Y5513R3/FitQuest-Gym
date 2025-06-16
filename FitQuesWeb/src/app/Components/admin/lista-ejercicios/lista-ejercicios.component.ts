import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { EjercicioCreateUpdateDto, GetEjercicioDto, Page } from '../../../models/ejercicio.model';
import { EjercicioService } from '../../../Services/ejercicio/ejercicio.service';
import { Router } from '@angular/router';
import { Nivel } from '../../../models/nivel.model';
import { NivelService } from '../../../Services/nivel/nivel.service';


@Component({
  selector: 'app-lista-ejercicios',
  templateUrl: './lista-ejercicios.component.html',
  styleUrl: './lista-ejercicios.component.css'
})
export class ListaEjerciciosComponent {

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

  constructor(
    private ejercicioService: EjercicioService,
    private nivelService: NivelService,
    private router: Router
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


  /*createEjercicio(): void {
    this.errorMessage = null;
    this.successMessage = null;

   
    if (!this.nombre || !this.descripcion || this.series === null || this.repeticiones === null ||
        this.duracion === null) { 
      this.errorMessage = 'Por favor, completa todos los campos requeridos (nombre, descripción, series, repeticiones, duración).';
      return;
    }
    if (this.series < 1 || this.repeticiones < 1 || this.duracion < 0) {
      this.errorMessage = 'Series, repeticiones y duración deben ser números positivos.';
      return;
    }

    this.nivelService.findById(this.selectedNivelId).subscribe(nivel =>{
      const niveles = nivel;
    })
    
    const newEjercicio: EjercicioCreateUpdateDto = {
      nombre: this.nombre,
      descripcion: this.descripcion,
      series: this.series,
      repeticiones: this.repeticiones,
      duracion: this.duracion,
      urlImagen: this.urlImagen || 'No hay',
      Nivel: this.nivel,
      
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

  }*/

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
}
