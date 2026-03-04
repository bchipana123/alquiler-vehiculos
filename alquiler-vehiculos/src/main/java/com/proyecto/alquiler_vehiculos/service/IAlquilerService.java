package com.proyecto.alquiler_vehiculos.service;

import com.proyecto.alquiler_vehiculos.dto.AlquilerDTO;
import com.proyecto.alquiler_vehiculos.dto.PageResponseDTO;

public interface IAlquilerService {

    PageResponseDTO<AlquilerDTO> listar(int page, int size,
                                        String sortBy, String sortDir,
                                        String busqueda, String estado);

    AlquilerDTO buscarPorId(Long id);

    AlquilerDTO crear(AlquilerDTO dto);

    AlquilerDTO actualizar(Long id, AlquilerDTO dto);

    AlquilerDTO actualizarParcial(Long id, AlquilerDTO dto);

    void eliminar(Long id);
}