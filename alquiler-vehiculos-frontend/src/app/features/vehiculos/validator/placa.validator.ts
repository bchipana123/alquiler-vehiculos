import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';



export function placaValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const valor = control.value;

    if (!valor) return null;

    const esValido = /^[A-Z0-9]{3}-\d{3}$/.test(valor);

    return esValido ? null : { placaInvalida: { value: valor } };
  };
}