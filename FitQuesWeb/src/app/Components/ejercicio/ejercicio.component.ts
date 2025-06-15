import { Component } from '@angular/core';
import { EjercicioService } from '../../Services/ejercicio/ejercicio.service';
import { GetEjercicioDto, Page } from '../../models/ejercicio.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-ejercicio',
  templateUrl: './ejercicio.component.html',
  styleUrl: './ejercicio.component.css'
})
export class EjercicioComponent {

  ejerciciosPage: Page<GetEjercicioDto> | undefined;
  currentPage: number = 0;
  pageSize: number = 6;
  searchTerm: string = '';

  constructor(
    private ejercicioService: EjercicioService,
    private router: Router
  ) { }

  ngOnInit(): void {
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

  navigateToEjercicioDetails(id: number): void {
    this.router.navigate(['/ejercicios', id]);
  }
}


