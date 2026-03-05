package com.proyecto.alquiler_vehiculos.modulo_clientes.controller;

import com.proyecto.alquiler_vehiculos.modulo_clientes.dto.ClienteDTO;
import com.proyecto.alquiler_vehiculos.paginacion.PageResponseDTO;
import com.proyecto.alquiler_vehiculos.modulo_clientes.service.IClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final IClienteService clienteService;

    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<ClienteDTO>> listar(
            @RequestParam(defaultValue = "0")      int page,
            @RequestParam(defaultValue = "10")     int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc")    String sortDir,
            @RequestParam(required = false)        String busqueda) {

        return ResponseEntity.ok(
                clienteService.listar(page, size, sortBy, sortDir, busqueda));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> crear(
            @Valid @RequestBody ClienteDTO clienteDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteService.crear(clienteDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteDTO clienteDTO) {
        return ResponseEntity.ok(clienteService.actualizar(id, clienteDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizarParcial(
            @PathVariable Long id,
            @RequestBody ClienteDTO clienteDTO) {
        return ResponseEntity.ok(
                clienteService.actualizarParcial(id, clienteDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}