import { Injectable } from '@angular/core';
import { GetMaterialDto, Page } from '../../models/material.model';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class MaterialService {

 private apiUrl = `http://localhost:8080/material`;

  constructor(private http: HttpClient) { }

  findAllMateriales(page: number = 0, size: number = 10, nombre?: string): Observable<Page<GetMaterialDto>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (nombre && nombre.trim() !== '') {
      params = params.set('nombre', nombre.trim());
    }

    return this.http.get<Page<GetMaterialDto>>(`${this.apiUrl}/all`, { params: params });
  }

  getMaterialById(id: number): Observable<GetMaterialDto> {
    return this.http.get<GetMaterialDto>(`${this.apiUrl}/${id}`);
  }

}