package com.proyecto.alquiler_vehiculos.modulo_clientes.repository;

import com.proyecto.alquiler_vehiculos.modulo_clientes.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByDni(String dni);

    boolean existsByDni(String dni);

    // ¿Existe OTRO cliente con ese DNI? (para validar en edición)
    // "AndIdNot" = que el id sea diferente al que estamos editando
    boolean existsByDniAndIdNot(String dni, Long id);

    // Búsqueda por nombre o DNI, sin distinguir mayúsculas
    @Query("SELECT c FROM Cliente c WHERE " +
            "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "c.dni LIKE CONCAT('%', :q, '%')")
    Page<Cliente> buscar(@Param("q") String q, Pageable pageable);
}