package com.proyecto.alquiler_vehiculos.modulo_clientes.service;

import com.proyecto.alquiler_vehiculos.modulo_clientes.dto.ClienteDTO;
import com.proyecto.alquiler_vehiculos.paginacion.PageResponseDTO;

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