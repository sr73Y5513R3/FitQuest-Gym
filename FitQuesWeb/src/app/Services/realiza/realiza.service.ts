import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CreateRealizaCmd, GetRealizaDto, Page } from '../../models/realiza.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RealizaService {

  private baseUrl = `http://localhost:8080/realizado`;

  constructor(private http: HttpClient) { }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt_token');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  entrenamientoRealizado(realizaCmd: CreateRealizaCmd): Observable<GetRealizaDto> {
    const headers = this.getAuthHeaders();
    return this.http.post<GetRealizaDto>(this.baseUrl, realizaCmd, { headers });
  }

  aceptarRealizado(idEntrenador: string, idUsuario: string, idEntrenamiento: number): Observable<GetRealizaDto> {
    const headers = this.getAuthHeaders();
    let params = new HttpParams();
    params = params.append('idEntrenador', idEntrenador);
    params = params.append('idUsuario', idUsuario);
    params = params.append('idEntrenamiento', idEntrenamiento.toString());

    return this.http.put<GetRealizaDto>(`${this.baseUrl}/aceptar`, null, { headers, params });
  }

  getAllSinRealizar(page: number = 0, size: number = 10): Observable<Page<GetRealizaDto>> {
    const headers = this.getAuthHeaders();
    let params = new HttpParams();
    params = params.append('page', page.toString());
    params = params.append('size', size.toString());

    return this.http.get<Page<GetRealizaDto>>(`${this.baseUrl}/all`, { headers, params });
  }

  getAllAceptados(page: number = 0, size: number = 10): Observable<Page<GetRealizaDto>> {
    const headers = this.getAuthHeaders();
    let params = new HttpParams();
    params = params.append('page', page.toString());
    params = params.append('size', size.toString());

    return this.http.get<Page<GetRealizaDto>>(`${this.baseUrl}/all/aceptados`, { headers, params });
  }

  getAllByUser(idUsuario: string, page: number = 0, size: number = 10): Observable<Page<GetRealizaDto>> {
    const headers = this.getAuthHeaders();
    let params = new HttpParams();
    params = params.append('page', page.toString());
    params = params.append('size', size.toString());

    return this.http.get<Page<GetRealizaDto>>(`${this.baseUrl}/all/${idUsuario}`, { headers, params });
  }
}
