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

        String clienteNombre,
        String clienteDni,

        @NotNull(message = "El vehículo es obligatorio")
        Long vehiculoId,

        String vehiculoPlaca,
        String vehiculoMarca,
        String vehiculoModelo,

        Double totalPagar
) {}