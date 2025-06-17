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

export interface EntrenoSimple {
  id: number;
  nombre: string;
  descripcion: string;
}

export interface Entrenador {
  id: string; 
  Username: string; 
  roles: string[]; 
  entrenos: EntrenoSimple[];
 
}

export interface GetClienteDto {
  id: string;
  username: string;
  nombre: string;
  apellido1: string;
  apellido2: string;
  email: string;
  roles: string[];
  peso: number;
  altura: number;
  edad: number;
  genero: string;
  mensualidad: string;
  nivelNombre: string;
}

export interface EditClienteCmd {
  nombre: string;
  apellido1: string;
  apellido2: string;
  email: string;
  username: string;
  peso: number;
  altura: number;
  edad: number;
  genero: string;
}

export interface EditEntrenadorCmd {
  nombre: string;
  apellido1: string;
  apellido2?: string; 
  email: string;
  username: string;
 
}

export interface Usuario {
  id: string;
  nombre: string;
  apellido1: string;
  apellido2?: string; 
  email: string;
  username: string;
  enabled: boolean;
}

export interface getEntenadorDto extends Usuario{
  id: string; 
  Username: string; 
  roles: string[]; 
  entrenos: EntrenoSimple[];
}