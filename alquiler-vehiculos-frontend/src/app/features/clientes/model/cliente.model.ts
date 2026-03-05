// Interfaz = define la "forma" del objeto Cliente
// El ? significa que el campo es opcional
// (id es opcional porque cuando CREAS no lo envías,
//  pero cuando EDITAS sí lo recibes del backend)

export interface Cliente {
    id?: number;
    nombre: string;
    dni: string;
    email: string;
    telefono: string;
  }