package com.proyecto.alquiler_vehiculos.controller;

import com.proyecto.alquiler_vehiculos.dto.PageResponseDTO;
import com.proyecto.alquiler_vehiculos.dto.VehiculoDTO;
import com.proyecto.alquiler_vehiculos.service.IVehiculoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final IVehiculoService vehiculoService;

    public VehiculoController(IVehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    // GET /api/vehiculos?estado=DISPONIBLE
    // GET /api/vehiculos?busqueda=toyota
    @GetMapping
    public ResponseEntity<PageResponseDTO<VehiculoDTO>> listar(
            @RequestParam(defaultValue = "0")     int page,
            @RequestParam(defaultValue = "10")    int size,
            @RequestParam(defaultValue = "marca") String sortBy,
            @RequestParam(defaultValue = "asc")   String sortDir,
            @RequestParam(required = false)       String busqueda,
            @RequestParam(required = false)       String estado) {

        return ResponseEntity.ok(
                vehiculoService.listar(page, size, sortBy, sortDir, busqueda, estado));
    }

    // GET /api/vehiculos/5
    @GetMapping("/{id}")
    public ResponseEntity<VehiculoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculoService.buscarPorId(id));
    }

    // POST /api/vehiculos
    @PostMapping
    public ResponseEntity<VehiculoDTO> crear(
            @Valid @RequestBody VehiculoDTO vehiculoDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vehiculoService.crear(vehiculoDTO));
    }

    // PUT /api/vehiculos/5
    @PutMapping("/{id}")
    public ResponseEntity<VehiculoDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody VehiculoDTO vehiculoDTO) {
        return ResponseEntity.ok(vehiculoService.actualizar(id, vehiculoDTO));
    }

    // PATCH /api/vehiculos/5
    @PatchMapping("/{id}")
    public ResponseEntity<VehiculoDTO> actualizarParcial(
            @PathVariable Long id,
            @RequestBody VehiculoDTO vehiculoDTO) {
        return ResponseEntity.ok(
                vehiculoService.actualizarParcial(id, vehiculoDTO));
    }

    // DELETE /api/vehiculos/5
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}