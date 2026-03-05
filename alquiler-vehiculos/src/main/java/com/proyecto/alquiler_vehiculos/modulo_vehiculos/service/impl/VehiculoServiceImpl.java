package com.proyecto.alquiler_vehiculos.modulo_vehiculos.service.impl;

import com.proyecto.alquiler_vehiculos.paginacion.PageResponseDTO;
import com.proyecto.alquiler_vehiculos.modulo_vehiculos.dto.VehiculoDTO;
import com.proyecto.alquiler_vehiculos.modulo_vehiculos.entity.EstadoVehiculo;
import com.proyecto.alquiler_vehiculos.modulo_vehiculos.entity.Vehiculo;
import com.proyecto.alquiler_vehiculos.exception.BusinessException;
import com.proyecto.alquiler_vehiculos.exception.ResourceNotFoundException;
import com.proyecto.alquiler_vehiculos.modulo_vehiculos.service.IVehiculoService;
import com.proyecto.alquiler_vehiculos.modulo_vehiculos.repository.VehiculoRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VehiculoServiceImpl implements IVehiculoService {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoServiceImpl(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<VehiculoDTO> listar(int page, int size,
                                               String sortBy, String sortDir,
                                               String busqueda, String estado) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Vehiculo> pagina;

        if (estado != null && !estado.isBlank()) {
            pagina = vehiculoRepository.findByEstado(
                    EstadoVehiculo.valueOf(estado.toUpperCase()), pageable);
        } else if (busqueda != null && !busqueda.isBlank()) {
            pagina = vehiculoRepository.buscar(busqueda, pageable);
        } else {
            pagina = vehiculoRepository.findAll(pageable);
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
    public VehiculoDTO buscarPorId(Long id) {
        return toDTO(vehiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo", id)));
    }

    @Override
    public VehiculoDTO crear(VehiculoDTO dto) {
        if (vehiculoRepository.existsByPlaca(dto.placa()))
            throw new BusinessException(
                    "Ya existe un vehículo con placa: " + dto.placa());

        Vehiculo vehiculo = toEntity(dto);
        vehiculo.setEstado(EstadoVehiculo.DISPONIBLE);
        return toDTO(vehiculoRepository.save(vehiculo));
    }

    @Override
    public VehiculoDTO actualizar(Long id, VehiculoDTO dto) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo", id));

        if (vehiculoRepository.existsByPlacaAndIdNot(dto.placa(), id))
            throw new BusinessException(
                    "Ya existe otro vehículo con placa: " + dto.placa());

        vehiculo.setPlaca(dto.placa());
        vehiculo.setMarca(dto.marca());
        vehiculo.setModelo(dto.modelo());
        vehiculo.setPrecioPorDia(dto.precioPorDia());
        if (dto.estado() != null) vehiculo.setEstado(dto.estado());

        return toDTO(vehiculoRepository.save(vehiculo));
    }

    @Override
    public VehiculoDTO actualizarParcial(Long id, VehiculoDTO dto) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo", id));

        if (dto.placa() != null) {
            if (vehiculoRepository.existsByPlacaAndIdNot(dto.placa(), id))
                throw new BusinessException(
                        "Ya existe otro vehículo con placa: " + dto.placa());
            vehiculo.setPlaca(dto.placa());
        }
        if (dto.marca() != null)        vehiculo.setMarca(dto.marca());
        if (dto.modelo() != null)       vehiculo.setModelo(dto.modelo());
        if (dto.precioPorDia() != null) vehiculo.setPrecioPorDia(dto.precioPorDia());
        if (dto.estado() != null)       vehiculo.setEstado(dto.estado());

        return toDTO(vehiculoRepository.save(vehiculo));
    }

    @Override
    public void eliminar(Long id) {
        if (!vehiculoRepository.existsById(id))
            throw new ResourceNotFoundException("Vehículo", id);
        vehiculoRepository.deleteById(id);
    }

    // ─── Conversores privados ───────────────────────

    private VehiculoDTO toDTO(Vehiculo vehiculo) {
        return new VehiculoDTO(
                vehiculo.getId(),
                vehiculo.getPlaca(),
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                vehiculo.getEstado(),
                vehiculo.getPrecioPorDia()
        );
    }

    private Vehiculo toEntity(VehiculoDTO dto) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(dto.placa());
        vehiculo.setMarca(dto.marca());
        vehiculo.setModelo(dto.modelo());
        vehiculo.setPrecioPorDia(dto.precioPorDia());
        return vehiculo;
    }
}