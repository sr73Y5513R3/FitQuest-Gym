// src/app/models/material.model.ts

export interface GetMaterialDto {
  id: number;
  nombre: string;
  descripcion: string;
  tipo: string; 
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
  first: boolean;
  numberOfElements: boolean; 
  empty: boolean;
}

export interface MaterialCreateUpdateDto {
  nombre: string;
  descripcion: string;
  tipo: string;
}