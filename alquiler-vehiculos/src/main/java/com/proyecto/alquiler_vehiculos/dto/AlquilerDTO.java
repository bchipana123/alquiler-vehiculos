package com.proyecto.alquiler_vehiculos.dto;

import com.proyecto.alquiler_vehiculos.entity.EstadoAlquiler;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AlquilerDTO(

        Long id,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate fechaInicio,

        @NotNull(message = "La fecha de fin es obligatoria")
        LocalDate fechaFin,

        EstadoAlquiler estado,

        @NotNull(message = "El cliente es obligatorio")
        Long clienteId,

        // Estos 2 campos solo se usan al DEVOLVER datos al frontend
        // para mostrar en la tabla sin hacer otra consulta
        String clienteNombre,
        String clienteDni,

        @NotNull(message = "El vehículo es obligatorio")
        Long vehiculoId,

        // Igual, solo para mostrar en la tabla
        String vehiculoPlaca,
        String vehiculoMarca,
        String vehiculoModelo,

        // Se calcula en el service: días × precio por día
        Double totalPagar
) {}