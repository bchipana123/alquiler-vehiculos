package com.proyecto.alquiler_vehiculos.service;

import com.proyecto.alquiler_vehiculos.dto.PageResponseDTO;
import com.proyecto.alquiler_vehiculos.dto.VehiculoDTO;

public interface IVehiculoService {

    PageResponseDTO<VehiculoDTO> listar(int page, int size,
                                        String sortBy, String sortDir,
                                        String busqueda, String estado);

    VehiculoDTO buscarPorId(Long id);

    VehiculoDTO crear(VehiculoDTO dto);

    VehiculoDTO actualizar(Long id, VehiculoDTO dto);

    VehiculoDTO actualizarParcial(Long id, VehiculoDTO dto);

    void eliminar(Long id);
}