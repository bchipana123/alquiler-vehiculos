import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';


export function fechaFinValidator(): ValidatorFn {
  return (group: AbstractControl): ValidationErrors | null => {
    const fechaInicio = group.get('fechaInicio')?.value;
    const fechaFin = group.get('fechaFin')?.value;

    if (!fechaInicio || !fechaFin) return null;

    const inicio = new Date(fechaInicio);
    const fin = new Date(fechaFin);

    return fin > inicio ? null : { fechaFinInvalida: true };
  };
}