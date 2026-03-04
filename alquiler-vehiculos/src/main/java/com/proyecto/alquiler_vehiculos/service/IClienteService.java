package com.proyecto.alquiler_vehiculos.service;

import com.proyecto.alquiler_vehiculos.dto.ClienteDTO;
import com.proyecto.alquiler_vehiculos.dto.PageResponseDTO;

public interface IClienteService {

    PageResponseDTO<ClienteDTO> listar(int page, int size,
                                       String sortBy, String sortDir,
                                       String busqueda);

    ClienteDTO buscarPorId(Long id);

    ClienteDTO crear(ClienteDTO dto);

    ClienteDTO actualizar(Long id, ClienteDTO dto);

    ClienteDTO actualizarParcial(Long id, ClienteDTO dto);

    void eliminar(Long id);
}