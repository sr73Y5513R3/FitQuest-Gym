import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { EntrenamientoCreateUpdateDto, GetEntrenoConEjercicioDto, Page } from '../../../models/entrenamiento.model';
import { UsuarioService } from '../../../Services/usuario/usuario.service';
import { EntrenamientoService } from '../../../Services/entrenamiento/entrenamiento.service';
import { Router } from '@angular/router';
import { Entrenador } from '../../../models/usuario.model';

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

  constructor(
    private entrenamientoService: EntrenamientoService,
    private usuarioService: UsuarioService,
    private router: Router
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
        // Opcional: Limpiar el formulario después de la creación
        this.nombre = '';
        this.descripcion = '';
        this.calorias = null;
        this.puntos = null;
        this.entrenadorId = '';

        // Redirigir a la lista de entrenamientos después de un tiempo
        setTimeout(() => {
          this.router.navigate(['/admin/entrenamientos']);
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

  goBack(): void {
    this.router.navigate(['/admin/entrenamientos']);
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
    if(this.create){
      this.create = false;
    }else{
      this.create = true;
    }
  }
}
