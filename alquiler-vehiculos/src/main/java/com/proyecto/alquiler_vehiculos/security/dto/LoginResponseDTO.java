package com.proyecto.alquiler_vehiculos.security.dto;

// Lo que devolvemos al frontend después del login exitoso
public record LoginResponseDTO(
        String token,     // el JWT que el frontend debe guardar
        String username,
        String rol
) {}