package com.proyecto.alquiler_vehiculos.service.impl;

import com.proyecto.alquiler_vehiculos.dto.AlquilerDTO;
import com.proyecto.alquiler_vehiculos.dto.PageResponseDTO;
import com.proyecto.alquiler_vehiculos.entity.*;
import com.proyecto.alquiler_vehiculos.exception.BusinessException;
import com.proyecto.alquiler_vehiculos.exception.ResourceNotFoundException;
import com.proyecto.alquiler_vehiculos.repository.*;
import com.proyecto.alquiler_vehiculos.service.IAlquilerService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
public class AlquilerServiceImpl implements IAlquilerService {

    private final AlquilerRepository alquilerRepository;
    private final ClienteRepository clienteRepository;
    private final VehiculoRepository vehiculoRepository;

    public AlquilerServiceImpl(AlquilerRepository alquilerRepository,
                               ClienteRepository clienteRepository,
                               VehiculoRepository vehiculoRepository) {
        this.alquilerRepository = alquilerRepository;
        this.clienteRepository  = clienteRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<AlquilerDTO> listar(int page, int size,
                                               String sortBy, String sortDir,
                                               String busqueda, String estado) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Alquiler> pagina;

        if (estado != null && !estado.isBlank()) {
            pagina = alquilerRepository.findByEstado(
                    EstadoAlquiler.valueOf(estado.toUpperCase()), pageable);
        } else if (busqueda != null && !busqueda.isBlank()) {
            pagina = alquilerRepository.buscar(busqueda, pageable);
        } else {
            pagina = alquilerRepository.findAll(pageable);
        }

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
    public AlquilerDTO buscarPorId(Long id) {
        return toDTO(alquilerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alquiler", id)));
    }

    @Override
    public AlquilerDTO crear(AlquilerDTO dto) {
        // Regla 1: fecha fin debe ser posterior a fecha inicio
        if (!dto.fechaFin().isAfter(dto.fechaInicio()))
            throw new BusinessException(
                    "La fecha de fin debe ser posterior a la de inicio");

        // Regla 2: el vehículo no puede tener ya un alquiler ACTIVO
        if (alquilerRepository.existsByVehiculoIdAndEstado(
                dto.vehiculoId(), EstadoAlquiler.ACTIVO))
            throw new BusinessException(
                    "El vehículo ya tiene un alquiler activo");

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente", dto.clienteId()));

        Vehiculo vehiculo = vehiculoRepository.findById(dto.vehiculoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehículo", dto.vehiculoId()));

        // Cambia estado del vehículo a ALQUILADO automáticamente
        vehiculo.setEstado(EstadoVehiculo.ALQUILADO);
        vehiculoRepository.save(vehiculo);

        Alquiler alquiler = new Alquiler();
        alquiler.setFechaInicio(dto.fechaInicio());
        alquiler.setFechaFin(dto.fechaFin());
        alquiler.setEstado(EstadoAlquiler.ACTIVO);
        alquiler.setCliente(cliente);
        alquiler.setVehiculo(vehiculo);

        return toDTO(alquilerRepository.save(alquiler));
    }

    @Override
    public AlquilerDTO actualizar(Long id, AlquilerDTO dto) {
        Alquiler alquiler = alquilerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alquiler", id));

        if (!dto.fechaFin().isAfter(dto.fechaInicio()))
            throw new BusinessException(
                    "La fecha de fin debe ser posterior a la de inicio");

        alquiler.setFechaInicio(dto.fechaInicio());
        alquiler.setFechaFin(dto.fechaFin());

        // Si cambia a FINALIZADO o CANCELADO → libera el vehículo
        if (dto.estado() != null && dto.estado() != EstadoAlquiler.ACTIVO) {
            alquiler.setEstado(dto.estado());
            alquiler.getVehiculo().setEstado(EstadoVehiculo.DISPONIBLE);
        }

        return toDTO(alquilerRepository.save(alquiler));
    }

    @Override
    public AlquilerDTO actualizarParcial(Long id, AlquilerDTO dto) {
        Alquiler alquiler = alquilerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alquiler", id));

        if (dto.estado() != null) {
            alquiler.setEstado(dto.estado());
            if (dto.estado() == EstadoAlquiler.FINALIZADO ||
                    dto.estado() == EstadoAlquiler.CANCELADO) {
                alquiler.getVehiculo().setEstado(EstadoVehiculo.DISPONIBLE);
            }
        }
        if (dto.fechaFin() != null) alquiler.setFechaFin(dto.fechaFin());

        return toDTO(alquilerRepository.save(alquiler));
    }

    @Override
    public void eliminar(Long id) {
        Alquiler alquiler = alquilerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alquiler", id));

        if (alquiler.getEstado() == EstadoAlquiler.ACTIVO)
            alquiler.getVehiculo().setEstado(EstadoVehiculo.DISPONIBLE);

        alquilerRepository.deleteById(id);
    }

    // ─── Conversores privados ───────────────────────

    private Double calcularTotal(Alquiler alquiler) {
        long dias = ChronoUnit.DAYS.between(
                alquiler.getFechaInicio(), alquiler.getFechaFin());
        return dias * alquiler.getVehiculo().getPrecioPorDia();
    }

    private AlquilerDTO toDTO(Alquiler alquiler) {
        return new AlquilerDTO(
                alquiler.getId(),
                alquiler.getFechaInicio(),
                alquiler.getFechaFin(),
                alquiler.getEstado(),
                alquiler.getCliente().getId(),
                alquiler.getCliente().getNombre(),
                alquiler.getCliente().getDni(),
                alquiler.getVehiculo().getId(),
                alquiler.getVehiculo().getPlaca(),
                alquiler.getVehiculo().getMarca(),
                alquiler.getVehiculo().getModelo(),
                calcularTotal(alquiler)
        );
    }
}
