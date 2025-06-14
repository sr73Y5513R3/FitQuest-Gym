import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EntrenamientoService } from '../../Services/entrenamiento/entrenamiento.service';
import { GetEntrenoConEjercicioDto } from '../../models/entrenamiento.model';

@Component({
  selector: 'app-entrenamiento-detalles',
  templateUrl: './entrenamiento-detalles.component.html',
  styleUrl: './entrenamiento-detalles.component.css'
})
export class EntrenamientoDetallesComponent implements OnInit {

  entrenamiento: GetEntrenoConEjercicioDto | undefined;
  errorMessage: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private entrenamientoService: EntrenamientoService
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id'); // Obtiene el 'id' de la URL
      if (id) {
        this.getEntrenamientoDetails(Number(id)); // Llama al servicio para obtener los detalles
      } else {
        // Si no hay ID en la URL, puedes redirigir o mostrar un error
        console.error('No se proporcionó un ID de entrenamiento.');
        this.errorMessage = 'No se encontró el entrenamiento. Por favor, especifique un ID válido.';
      }
    });
  }

  getEntrenamientoDetails(id: number): void {
    this.entrenamientoService.getEntrenamientoById(id).subscribe({
      next: (data) => {
        this.entrenamiento = data;
        this.errorMessage = null; // Limpiar cualquier error previo
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error al obtener detalles del entrenamiento:', err);
        this.entrenamiento = undefined; // Limpiar datos si hay error
        if (err.status === 404) {
          this.errorMessage = 'El entrenamiento solicitado no fue encontrado.';
        } else {
          this.errorMessage = `Error al cargar los detalles: ${err.statusText || 'Error desconocido'}.`;
        }
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/entrenamientos']); // Redirige a la lista principal de entrenamientos
  }
}


