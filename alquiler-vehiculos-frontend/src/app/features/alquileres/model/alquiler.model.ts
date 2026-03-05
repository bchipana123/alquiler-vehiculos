export enum EstadoAlquiler {
    ACTIVO = 'ACTIVO',
    FINALIZADO = 'FINALIZADO',
    CANCELADO = 'CANCELADO'
  }
  
  export interface Alquiler {
    id?: number;
    fechaInicio: string; 
    fechaFin: string;
    estado: EstadoAlquiler;
    clienteId: number;
    clienteNombre?: string; 
    clienteDni?: string;
    vehiculoId: number;
    vehiculoPlaca?: string;
    vehiculoMarca?: string;
    vehiculoModelo?: string;
    totalPagar?: number;
  }