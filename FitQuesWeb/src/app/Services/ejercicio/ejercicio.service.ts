import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { EjercicioCreateUpdateDto, GetEjercicioDto, Page } from '../../models/ejercicio.model';

@Injectable({
  providedIn: 'root'
})
export class EjercicioService {

 private apiUrl = `http://localhost:8080/ejercicio`;

  constructor(private http: HttpClient) { }

  findAllEjercicios(page: number = 0, size: number = 10, nombre?: string): Observable<Page<GetEjercicioDto>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (nombre && nombre.trim() !== '') {
      params = params.set('nombre', nombre.trim());
    }

    return this.http.get<Page<GetEjercicioDto>>(`${this.apiUrl}/all`, { params: params });
  }

  getEjercicioById(id: number): Observable<GetEjercicioDto> {
    return this.http.get<GetEjercicioDto>(`${this.apiUrl}/${id}`);
  }

  createEjercicio(ejercicioDto: EjercicioCreateUpdateDto): Observable<GetEjercicioDto> {
    return this.http.post<GetEjercicioDto>(`${this.apiUrl}/add`, ejercicioDto);
  }

  updateEjercicio(id: number, ejercicioDto: EjercicioCreateUpdateDto): Observable<GetEjercicioDto> {
    return this.http.put<GetEjercicioDto>(`${this.apiUrl}/edit/${id}`, ejercicioDto);
  }

  deleteEjercicio(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

  addMaterialToEjercicio(ejercicioId: number, materialId: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${ejercicioId}/material/${materialId}`, {}); 
  }

  removeMaterialFromEjercicio(ejercicioId: number, materialId: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${ejercicioId}/material/${materialId}`);
  }
}
