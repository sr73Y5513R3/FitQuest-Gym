import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Nivel, Page } from '../../models/nivel.model';

@Injectable({
  providedIn: 'root'
})
export class NivelService {

  private baseUrl = 'http://localhost:8080/nivel';

  constructor(private http: HttpClient) { }

  findAll(): Observable<Nivel[]> {
    let params = new HttpParams()
      .set('page', '0')
      .set('size', '1000');

    return this.http.get<Page<Nivel>>(`${this.baseUrl}/all`, { params }).pipe(
      map(response => response.content)
    );
  }

   findById(id: number): Observable<Nivel> {
    return this.http.get<Nivel>(`${this.baseUrl}/${id}`); 
  }
}
