package com.proyecto.alquiler_vehiculos.dto;

import com.proyecto.alquiler_vehiculos.entity.EstadoVehiculo;
import jakarta.validation.constraints.*;

public record VehiculoDTO(

        Long id,

        @NotBlank(message = "La placa es obligatoria")
        @Pattern(regexp = "[A-Z0-9]{3}-\\d{3}", message = "Formato de placa: ABC-123")
        String placa,

        @NotBlank(message = "La marca es obligatoria")
        String marca,

        @NotBlank(message = "El modelo es obligatorio")
        String modelo,

        EstadoVehiculo estado,

        @NotNull(message = "El precio por día es obligatorio")
        @Positive(message = "El precio debe ser mayor a 0")
        Double precioPorDia
) {}