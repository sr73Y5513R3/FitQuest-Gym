import { Component } from '@angular/core';
import { GetEjercicioDto } from '../../models/ejercicio.model';
import { ActivatedRoute, Router } from '@angular/router';
import { EjercicioService } from '../../Services/ejercicio/ejercicio.service';

@Component({
  selector: 'app-ejercicio-detalles',
  templateUrl: './ejercicio-detalles.component.html',
  styleUrl: './ejercicio-detalles.component.css'
})
export class EjercicioDetallesComponent {


   ejercicio: GetEjercicioDto | undefined; 
  isLoading: boolean = true; 
  errorMessage: string | undefined; 
  constructor(
    private route: ActivatedRoute, 
    private ejercicioService: EjercicioService,
    private router: Router 
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const idString = params.get('id');
      if (idString) {
        const id = +idString; 
        this.getEjercicioDetails(id);
      } else {
        this.errorMessage = 'ID de ejercicio no proporcionado.';
        this.isLoading = false;
      
      }
    });
  }

  getEjercicioDetails(id: number): void {
    this.isLoading = true;
    this.errorMessage = undefined; // Limpiar cualquier error previo

    this.ejercicioService.getEjercicioById(id).subscribe({
      next: (data) => {
        this.ejercicio = data;
        this.isLoading = false;
        console.log('Detalles del ejercicio cargados:', this.ejercicio);
      },
      error: (err) => {
        console.error('Error al cargar los detalles del ejercicio:', err);
        this.errorMessage = 'No se pudieron cargar los detalles del ejercicio. Inténtalo de nuevo más tarde.';
        this.isLoading = false;
        
      }
    });
  }


  goBackToList(): void {
    this.router.navigate(['/ejercicios']);
  }

  navigateToMaterialDetail(MaterialId: number) : void {
    this.router.navigate(['/materiales', MaterialId]);
}
}

