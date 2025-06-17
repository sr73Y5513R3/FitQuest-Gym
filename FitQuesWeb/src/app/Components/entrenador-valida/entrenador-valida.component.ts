import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { RealizaService } from '../../Services/realiza/realiza.service';
import { AuthService } from '../../Services/login/auth-service.service';
import { GetRealizaDto, Page } from '../../models/realiza.model';

@Component({
  selector: 'app-entrenador-valida',
  templateUrl: './entrenador-valida.component.html',
  styleUrl: './entrenador-valida.component.css'
})
export class EntrenadorValidaComponent {
 realizasPage: Page<GetRealizaDto> | undefined;
  currentPage: number = 0;
  pageSize: number = 10; 

  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(
    private realizaService: RealizaService,
    private authService: AuthService 
  ) { }

  ngOnInit(): void {
    this.loadRealizasPendientes();
  }

  loadRealizasPendientes(): void {
    this.realizaService.getAllSinRealizar(this.currentPage, this.pageSize)
      .subscribe({
        next: (data) => {
          this.realizasPage = data;
          console.log('Entrenamientos pendientes de validación cargados:', this.realizasPage);
          this.errorMessage = null; 
        },
        error: (err) => {
          console.error('Error al cargar entrenamientos pendientes:', err);
          this.errorMessage = 'No se pudieron cargar los entrenamientos pendientes. Inténtalo de nuevo.';
          setTimeout(() => this.errorMessage = null, 5000); 
        }
      });
  }

  aceptarEntrenamiento(idUsuario: string, idEntrenamiento: number): void {
    this.successMessage = null; 
    this.errorMessage = null;

    const idEntrenador = this.authService.getLoggedInUserId(); 
    if (!idEntrenador) {
      this.errorMessage = 'No se pudo obtener el ID del entrenador. Asegúrate de estar logueado como entrenador.';
      setTimeout(() => this.errorMessage = null, 5000);
      return;
    }

    this.realizaService.aceptarRealizado(idEntrenador, idUsuario, idEntrenamiento).subscribe({
      next: (response) => {
        this.successMessage = `Entrenamiento de ${response.usuario.username} (${response.entreno.nombre}) aceptado con éxito.`;
        console.log('Entrenamiento aceptado:', response);
        this.loadRealizasPendientes(); 
        setTimeout(() => this.successMessage = null, 5000);
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error al aceptar entrenamiento:', err);
        if (err.status === 401 || err.status === 403) {
          this.errorMessage = 'No tienes permiso para realizar esta acción. Asegúrate de tener el rol de Entrenador.';
        } else if (err.status === 404) {
          this.errorMessage = 'El entrenamiento realizado no fue encontrado (quizás ya fue aceptado o hay IDs incorrectos).';
        } else if (err.status === 400) {
          this.errorMessage = 'Solicitud de aceptación inválida. Verifica los datos.';
        } else {
          this.errorMessage = 'Ocurrió un error al aceptar el entrenamiento. Inténtalo de nuevo.';
        }
        setTimeout(() => this.errorMessage = null, 5000);
      }
    });
  }

  goToPage(page: number): void {
    this.currentPage = page;
    this.loadRealizasPendientes();
  }

  nextPage(): void {
    if (this.realizasPage && !this.realizasPage.last) {
      this.currentPage++;
      this.loadRealizasPendientes();
    }
  }

  previousPage(): void {
    if (this.realizasPage && !this.realizasPage.first) {
      this.currentPage--;
      this.loadRealizasPendientes();
    }
  }
}
