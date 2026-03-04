package com.proyecto.alquiler_vehiculos.exception;

// Se lanza cuando se viola una regla de negocio
// Ejemplo: intentar crear un cliente con DNI repetido
// Ejemplo: intentar alquilar un vehículo ya alquilado
public class BusinessException extends RuntimeException {
    public BusinessException(String mensaje) {
        super(mensaje);
    }
}