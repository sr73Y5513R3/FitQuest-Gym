import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Page } from '../../models/entrenamiento.model';
import { Entrenador } from '../../models/usuario.model';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private baseUrl = 'http://localhost:8080';
  private apiUrl = `${this.baseUrl}/entrenador`; 

  constructor(private http: HttpClient) { }

  /**
   * Obtiene una página de objetos Entrenador desde el backend.
   * Ataca el endpoint `{{UrlVariable}}entrenador/all`
   * @param page El número de página a solicitar (0-indexado por defecto en Spring Data JPA).
   * @param size El tamaño de la página (número de elementos por página).
   * @returns Un Observable que emite un objeto Page<Entrenador>.
   */
  findAllEntrenadores(page: number = 0, size: number = 20): Observable<Page<Entrenador>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<Page<Entrenador>>(`${this.apiUrl}/all`, { params: params });
  }
}
