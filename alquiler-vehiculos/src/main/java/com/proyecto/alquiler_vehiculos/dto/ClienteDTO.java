package com.proyecto.alquiler_vehiculos.dto;

import jakarta.validation.constraints.*;

public record ClienteDTO(

        Long id,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombre,

        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos")
        String dni,

        @Email(message = "El email no tiene formato válido")
        String email,

        @Pattern(regexp = "9\\d{8}", message = "El teléfono debe empezar con 9 y tener 9 dígitos")
        String telefono
) {

}