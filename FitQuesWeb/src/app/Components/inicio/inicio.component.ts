import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../Services/login/auth-service.service';
import { EntrenamientoService } from '../../Services/entrenamiento/entrenamiento.service';
import { GetEntrenoConEjercicioDto } from '../../models/entrenamiento.model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-inicio',
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.scss']
})
export class InicioComponent implements OnInit {

  username: string | null = null;

  allEntrenamientos: GetEntrenoConEjercicioDto[] = [];
  principianteEntrenamientos: GetEntrenoConEjercicioDto[] = [];
  intermedioEntrenamientos: GetEntrenoConEjercicioDto[] = [];
  avanzadoEntrenamientos: GetEntrenoConEjercicioDto[] = [];

  featuredWorkouts: GetEntrenoConEjercicioDto[] = [];

  currentHomeView: 'all' | 'principiante' | 'intermedio' | 'avanzado' = 'all';

  errorMessage: string | null = null;

  constructor(
    private authService: AuthService,
    private entrenamientoService: EntrenamientoService
  ) { }

  ngOnInit(): void {
    this.username = this.authService.getUsername();
    if (this.username) {
      console.log(`¡Bienvenido a la pantalla de inicio, ${this.username}!`);
    }
    this.loadEntrenamientos();
  }

  logout(): void {
    this.authService.logout();
  }

  loadEntrenamientos(): void {
    this.entrenamientoService.findAllEntrenamientos(0, 1000, '')
      .subscribe({
        next: (pageData) => {
          this.allEntrenamientos = pageData.content;
          this.classifyEntrenamientos();
          this.selectFeaturedWorkouts();
          this.errorMessage = null;
        },
        error: (err: HttpErrorResponse) => {
          console.error('Error al cargar los entrenamientos:', err);
          this.errorMessage = 'No se pudieron cargar los entrenamientos. Inténtalo de nuevo.';
          setTimeout(() => this.errorMessage = null, 5000);
        }
      });
  }

  classifyEntrenamientos(): void {
    this.principianteEntrenamientos = this.allEntrenamientos.filter(
      entreno => entreno.nivel?.nombre?.toUpperCase() === 'PRINCIPIANTE'
    );
    this.intermedioEntrenamientos = this.allEntrenamientos.filter(
      entreno => entreno.nivel?.nombre?.toUpperCase() === 'INTERMEDIO'
    );
    this.avanzadoEntrenamientos = this.allEntrenamientos.filter(
      entreno => entreno.nivel?.nombre?.toUpperCase() === 'AVANZADO'
    );
  }

  selectFeaturedWorkouts(): void {
    const sortedEntrenamientos = [...this.allEntrenamientos].sort((a, b) => {
      return (b.valoracionMedia || 0) - (a.valoracionMedia || 0);
    });
    this.featuredWorkouts = sortedEntrenamientos.slice(0, 3);
  }

  get currentDisplayWorkouts(): GetEntrenoConEjercicioDto[] {
    switch (this.currentHomeView) {
      case 'principiante':
        return this.principianteEntrenamientos;
      case 'intermedio':
        return this.intermedioEntrenamientos;
      case 'avanzado':
        return this.avanzadoEntrenamientos;
      case 'all':
      default:
        return this.allEntrenamientos.slice(0, 3);
    }
  }

  setView(view: 'all' | 'principiante' | 'intermedio' | 'avanzado'): void {
    this.currentHomeView = view;
  }
}