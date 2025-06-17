export interface GetRealizaDto {
  realizado: boolean;
  entreno: {
    id: number;
    nombre: string;
  };
  usuario: {
    id: string;
    username: string;
  };
}

export interface CreateRealizaCmd {
  idUsuario: string;
  idEntrenamiento: number;
}

export interface Page<T> {
  content: T[];
  pageable: {
    pageNumber: number;
    pageSize: number;
  };
  totalPages: number;
  totalElements: number;
  last: boolean;
  first: boolean;
  size: number;
  number: number;
  numberOfElements: number;
  empty: boolean;
}