package com.proyecto.alquiler_vehiculos.paginacion;

import java.util.List;

public record PageResponseDTO<T>(

        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
) {}