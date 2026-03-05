package com.proyecto.alquiler_vehiculos.security.dto;

public record LoginResponseDTO(
        String token,
        String username,
        String rol
) {}