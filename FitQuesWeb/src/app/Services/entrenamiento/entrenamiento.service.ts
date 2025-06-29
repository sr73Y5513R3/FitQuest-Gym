// src/app/services/entrenamiento.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { catchError, Observable } from 'rxjs';
import { EntrenamientoCreateUpdateDto, GetEntrenoConEjercicioDto, Page } from '../../models/entrenamiento.model';

interface CreateValoracionCommand {
  idUsuario: string;
  idEntrenamiento: number; 
  notaValoracion: number;
}

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

  createEntrenamiento(entrenamientoDto: EntrenamientoCreateUpdateDto): Observable<GetEntrenoConEjercicioDto> { // Use GetEntrenoConEjercicioDto or a more appropriate type for the response
    return this.http.post<GetEntrenoConEjercicioDto>(`${this.apiUrl}/add`, entrenamientoDto);
  }

  updateEntrenamiento(id: number, entrenamientoDto: EntrenamientoCreateUpdateDto): Observable<GetEntrenoConEjercicioDto> { // Use GetEntrenoConEjercicioDto or a more appropriate type
    return this.http.put<GetEntrenoConEjercicioDto>(`${this.apiUrl}/edit/${id}`, entrenamientoDto);
  }

  deleteEntrenamiento(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

   addEjercicioToEntrenamiento(entrenamientoId: number, ejercicioId: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${entrenamientoId}/ejercicio/${ejercicioId}`, {}); 
  }

  deleteEjercicioToEntrenamiento(entrenamientoId: number, ejercicioId: number): Observable<any> {
    return this.http.delete<void>(`${this.apiUrl}/${entrenamientoId}/ejercicio/${ejercicioId}`, {}); 
  }

findAllEntrenamientosByEntrenador(
    idEntrenador: string,
    page: number = 0,
    size: number = 10,
    searchTerm: string = ''
  ): Observable<Page<GetEntrenoConEjercicioDto>> {

    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (searchTerm.trim() !== '') {
      
      params = params.set('searchTerm', searchTerm.trim());
    }

    return this.http.get<Page<GetEntrenoConEjercicioDto>>(`${this.apiUrl}/all/entrenador/${idEntrenador}`, { params });
  }

  addValoracion(idUsuario: string, idEntreno: number, notaValoracion: number): Observable<any> {
    const url = `http://localhost:8080/valoracion/add`;
    
    const body: CreateValoracionCommand = {
      idUsuario: idUsuario,
      idEntrenamiento: idEntreno, 
      notaValoracion: notaValoracion
    };
    
    return this.http.post(url, body)
  }
}
