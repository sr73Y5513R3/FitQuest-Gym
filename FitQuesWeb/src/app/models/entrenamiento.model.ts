// src/app/core/models/entrenamiento.model.ts

import { GetEjercicioDto } from "./ejercicio.model";

export interface EjercicioDto {
  id: number;
  nombre: string;
}

export interface EntrenadorDto {
  id: string;
  username: string;
}

export interface GetEntrenoConEjercicioDto {
  id: number;
  nombre: string;
  descripcion: string;
  duracion: number;
  calorias: number;
  puntos: number;
  valoracionMedia: number;
  entrenador: EntrenadorDto;
  ejercicios: GetEjercicioDto[];
}

export interface Page<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}

export interface EntrenamientoCreateUpdateDto {
  nombre: string;
  descripcion: string;
  calorias: number;
  puntos: number;
  entrenadorId: string; 
}