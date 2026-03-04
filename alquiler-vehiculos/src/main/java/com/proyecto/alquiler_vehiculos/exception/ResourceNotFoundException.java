package com.proyecto.alquiler_vehiculos.exception;

// Se lanza cuando buscas algo por ID y no existe en la BD
// Ejemplo: GET /api/clientes/999 → lanza esta excepción → devuelve 404
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String recurso, Long id) {
        super(recurso + " con ID " + id + " no encontrado");
    }
}