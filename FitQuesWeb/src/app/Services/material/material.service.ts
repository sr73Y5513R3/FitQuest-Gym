import { Injectable } from '@angular/core';
import { GetMaterialDto, MaterialCreateUpdateDto, Page } from '../../models/material.model';
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

   createMaterial(material: MaterialCreateUpdateDto): Observable<GetMaterialDto> {
    return this.http.post<GetMaterialDto>(`${this.apiUrl}/add`, material);
  }

  updateMaterial(id: number, material: MaterialCreateUpdateDto): Observable<GetMaterialDto> {
    return this.http.put<GetMaterialDto>(`${this.apiUrl}/edit/${id}`, material);
  }

  deleteMaterial(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

}