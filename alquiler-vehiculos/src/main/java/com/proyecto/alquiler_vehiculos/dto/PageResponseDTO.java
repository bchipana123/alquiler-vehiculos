package com.proyecto.alquiler_vehiculos.dto;

import java.util.List;

// <T> = genérico, funciona para cualquier tipo:
// PageResponseDTO<ClienteDTO>, PageResponseDTO<VehiculoDTO>, etc.
public record PageResponseDTO<T>(

        List<T> content,        // los registros de esta página
        int page,               // página actual (empieza en 0)
        int size,               // cuántos registros por página
        long totalElements,     // total de registros en la BD
        int totalPages,         // total de páginas
        boolean last            // ¿es la última página?
) {}