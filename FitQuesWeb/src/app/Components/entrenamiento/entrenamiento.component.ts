import { Component, OnInit } from '@angular/core';
import { EntrenamientoService } from '../../Services/entrenamiento/entrenamiento.service';
import { GetEntrenoConEjercicioDto, Page } from '../../models/entrenamiento.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-entrenamiento',
  templateUrl: './entrenamiento.component.html',
  styleUrl: './entrenamiento.component.css'
})
export class EntrenamientoComponent implements OnInit {

  entrenamientosPage: Page<GetEntrenoConEjercicioDto> | undefined;
  currentPage: number = 0;
  pageSize: number = 6;
  searchTerm: string = '';

  constructor(private entrenamientoService: EntrenamientoService, private router: Router) { }

  ngOnInit(): void {
    this.loadEntrenamientos();
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

  navigateToEntrenamientoDetails(id: number): void {
    // Esto construirá la URL como /entrenamientos/ID_DEL_ENTRENAMIENTO (ej. /entrenamientos/51)
    this.router.navigate(['/entrenamientos', id]);
  }
  
}
