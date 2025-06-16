// src/app/models/ejercicio.interface.ts

import { Nivel } from "./nivel.model";


export interface Material {
  id: number;
  nombre: string;
  
}

export interface GetEjercicioDto {
  id: number;
  nombre: string;
  descripcion: string;
  series: number;
  repeticiones: number;
  duracion: number; 
  urlImagen: string;
  nivel: Nivel;
  materiales: Material[];
}


export interface Page<T> {
  content: T[];
  pageable: {
    pageNumber: number;
    pageSize: number;
    sort: {
      empty: boolean;
      sorted: boolean;
      unsorted: boolean;
    };
    offset: number;
    paged: boolean;
    unpaged: boolean;
  };
  last: boolean;
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}

export interface EjercicioCreateUpdateDto {
  nombre: string;
  descripcion: string;
  series: number;
  repeticiones: number;
  duracion: number;
  urlImagen: string;
  nivel: Nivel;
}