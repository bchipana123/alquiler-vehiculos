
export enum EstadoVehiculo {
    DISPONIBLE = 'DISPONIBLE',
    ALQUILADO = 'ALQUILADO'
  }
  
  export interface Vehiculo {
    id?: number;
    placa: string;
    marca: string;
    modelo: string;
    estado: EstadoVehiculo;
    precioPorDia: number;
  }