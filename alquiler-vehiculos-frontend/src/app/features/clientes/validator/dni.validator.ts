import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';


export function dniValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const valor = control.value;

    if (!valor) return null; 

    const esValido = /^\d{8}$/.test(valor);

    return esValido ? null : { dniInvalido: { value: valor } };
  };
}