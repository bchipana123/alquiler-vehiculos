package com.proyecto.alquiler_vehiculos.modulo_clientes.service.impl;

import com.proyecto.alquiler_vehiculos.modulo_clientes.dto.ClienteDTO;
import com.proyecto.alquiler_vehiculos.paginacion.PageResponseDTO;
import com.proyecto.alquiler_vehiculos.modulo_clientes.entity.Cliente;
import com.proyecto.alquiler_vehiculos.exception.BusinessException;
import com.proyecto.alquiler_vehiculos.exception.ResourceNotFoundException;
import com.proyecto.alquiler_vehiculos.modulo_clientes.repository.ClienteRepository;
import com.proyecto.alquiler_vehiculos.modulo_clientes.service.IClienteService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClienteServiceImpl implements IClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<ClienteDTO> listar(int page, int size,
                                              String sortBy, String sortDir,
                                              String busqueda) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Cliente> pagina = (busqueda != null && !busqueda.isBlank())
                ? clienteRepository.buscar(busqueda, pageable)
                : clienteRepository.findAll(pageable);

        return new PageResponseDTO<>(
                pagina.getContent().stream().map(this::toDTO).toList(),
                pagina.getNumber(),
                pagina.getSize(),
                pagina.getTotalElements(),
                pagina.getTotalPages(),
                pagina.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteDTO buscarPorId(Long id) {
        return toDTO(clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id)));
    }

    @Override
    public ClienteDTO crear(ClienteDTO dto) {
        if (clienteRepository.existsByDni(dto.dni()))
            throw new BusinessException(
                    "Ya existe un cliente con DNI: " + dto.dni());

        return toDTO(clienteRepository.save(toEntity(dto)));
    }

    @Override
    public ClienteDTO actualizar(Long id, ClienteDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));

        if (clienteRepository.existsByDniAndIdNot(dto.dni(), id))
            throw new BusinessException(
                    "Ya existe otro cliente con DNI: " + dto.dni());

        cliente.setNombre(dto.nombre());
        cliente.setDni(dto.dni());
        cliente.setEmail(dto.email());
        cliente.setTelefono(dto.telefono());

        return toDTO(clienteRepository.save(cliente));
    }

    @Override
    public ClienteDTO actualizarParcial(Long id, ClienteDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));

        if (dto.nombre() != null)   cliente.setNombre(dto.nombre());
        if (dto.email() != null)    cliente.setEmail(dto.email());
        if (dto.telefono() != null) cliente.setTelefono(dto.telefono());
        if (dto.dni() != null) {
            if (clienteRepository.existsByDniAndIdNot(dto.dni(), id))
                throw new BusinessException(
                        "Ya existe otro cliente con DNI: " + dto.dni());
            cliente.setDni(dto.dni());
        }

        return toDTO(clienteRepository.save(cliente));
    }

    @Override
    public void eliminar(Long id) {
        if (!clienteRepository.existsById(id))
            throw new ResourceNotFoundException("Cliente", id);
        clienteRepository.deleteById(id);
    }

    // ─── Conversores privados ───────────────────────

    private ClienteDTO toDTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getDni(),
                cliente.getEmail(),
                cliente.getTelefono()
        );
    }

    private Cliente toEntity(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.nombre());
        cliente.setDni(dto.dni());
        cliente.setEmail(dto.email());
        cliente.setTelefono(dto.telefono());
        return cliente;
    }
}