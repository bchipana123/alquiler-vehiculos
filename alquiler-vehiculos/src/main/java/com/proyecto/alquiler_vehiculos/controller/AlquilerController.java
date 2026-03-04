package com.proyecto.alquiler_vehiculos.controller;

import com.proyecto.alquiler_vehiculos.dto.AlquilerDTO;
import com.proyecto.alquiler_vehiculos.dto.PageResponseDTO;
import com.proyecto.alquiler_vehiculos.service.IAlquilerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alquileres")
public class AlquilerController {

    private final IAlquilerService alquilerService;

    public AlquilerController(IAlquilerService alquilerService) {
        this.alquilerService = alquilerService;
    }

    // GET /api/alquileres?estado=ACTIVO
    // GET /api/alquileres?busqueda=juan
    @GetMapping
    public ResponseEntity<PageResponseDTO<AlquilerDTO>> listar(
            @RequestParam(defaultValue = "0")           int page,
            @RequestParam(defaultValue = "10")          int size,
            @RequestParam(defaultValue = "fechaInicio") String sortBy,
            @RequestParam(defaultValue = "desc")        String sortDir,
            @RequestParam(required = false)             String busqueda,
            @RequestParam(required = false)             String estado) {

        return ResponseEntity.ok(
                alquilerService.listar(page, size, sortBy, sortDir, busqueda, estado));
    }

    // GET /api/alquileres/5
    @GetMapping("/{id}")
    public ResponseEntity<AlquilerDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alquilerService.buscarPorId(id));
    }

    // POST /api/alquileres
    @PostMapping
    public ResponseEntity<AlquilerDTO> crear(
            @Valid @RequestBody AlquilerDTO alquilerDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(alquilerService.crear(alquilerDTO));
    }

    // PUT /api/alquileres/5
    @PutMapping("/{id}")
    public ResponseEntity<AlquilerDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AlquilerDTO alquilerDTO) {
        return ResponseEntity.ok(alquilerService.actualizar(id, alquilerDTO));
    }

    // PATCH /api/alquileres/5 → útil para solo cambiar el estado
    @PatchMapping("/{id}")
    public ResponseEntity<AlquilerDTO> actualizarParcial(
            @PathVariable Long id,
            @RequestBody AlquilerDTO alquilerDTO) {
        return ResponseEntity.ok(
                alquilerService.actualizarParcial(id, alquilerDTO));
    }

    // DELETE /api/alquileres/5
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        alquilerService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
