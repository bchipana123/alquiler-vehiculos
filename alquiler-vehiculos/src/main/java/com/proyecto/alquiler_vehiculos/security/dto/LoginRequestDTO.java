package com.proyecto.alquiler_vehiculos.security.dto;

import jakarta.validation.constraints.NotBlank;

// Lo que envía el frontend al hacer login
public record LoginRequestDTO(
        @NotBlank(message = "El usuario es obligatorio")
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {}