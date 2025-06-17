import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Page } from '../../models/entrenamiento.model';
import { EditClienteCmd, EditEntrenadorCmd, Entrenador, GetClienteDto, Usuario } from '../../models/usuario.model';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private baseUrl = 'http://localhost:8080';
  private apiUrlEntrenador = `${this.baseUrl}/entrenador`; 
  private apiUrlCliente = `${this.baseUrl}/cliente`; 

  constructor(private http: HttpClient) { }


  findAllEntrenadores(page: number = 0, size: number = 20): Observable<Page<Entrenador>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<Page<Entrenador>>(`${this.apiUrlEntrenador}/all`, { params: params });
  }

  getClienteById(id: string): Observable<GetClienteDto> {
    return this.http.get<GetClienteDto>(`${this.apiUrlCliente}/${id}`)
  }

  updateCliente(id: string, clienteData: EditClienteCmd): Observable<GetClienteDto> {
    return this.http.put<GetClienteDto>(`${this.apiUrlCliente}/edit/${id}`, clienteData)
  }

  editEntrenador(idEntrenador: string, editEntrenadorData: EditEntrenadorCmd): Observable<Entrenador> {
    return this.http.put<Entrenador>(`${this.apiUrlEntrenador}/${idEntrenador}`, editEntrenadorData);
  }

  darDeBaja(idUsuario: string): Observable<Usuario> {
    const updatePayload = { enabled: false };
    return this.http.patch<Usuario>(`${this.baseUrl}usuario/baja/${idUsuario}`, updatePayload);
  }

  findAll(page: number, size: number, sort?: string): Observable<Page<Usuario>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (sort) {
      params = params.set('sort', sort);
    }

    return this.http.get<Page<Usuario>>(`${this.baseUrl}usuario/all`, { params });
  }

  findAllClientes(page: number, size: number, sort?: string): Observable<Page<GetClienteDto>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (sort) {
      params = params.set('sort', sort);
    }

    return this.http.get<Page<GetClienteDto>>(`${this.apiUrlCliente}/all`, { params });
  }
}
