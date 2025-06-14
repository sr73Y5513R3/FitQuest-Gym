// src/app/services/entrenamiento.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GetEntrenoConEjercicioDto, Page } from '../../models/entrenamiento.model';

@Injectable({
  providedIn: 'root'
})
export class EntrenamientoService {
  private apiUrl = 'http://localhost:8080/entrenamiento'; 

  constructor(private http: HttpClient) { }

  findAllEntrenamientos(page: number = 0, size: number = 10, nombre?: string): Observable<Page<GetEntrenoConEjercicioDto>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (nombre && nombre.trim() !== '') {
      params = params.set('nombre', nombre.trim()); 
    }

    return this.http.get<Page<GetEntrenoConEjercicioDto>>(`${this.apiUrl}/all`, { params: params });
  }

  getEntrenamientoById(id: number): Observable<GetEntrenoConEjercicioDto> {
    return this.http.get<GetEntrenoConEjercicioDto>(`${this.apiUrl}/${id}`);
  }
}
