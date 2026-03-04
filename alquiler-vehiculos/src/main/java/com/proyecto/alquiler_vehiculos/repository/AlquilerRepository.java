package com.proyecto.alquiler_vehiculos.repository;

import com.proyecto.alquiler_vehiculos.entity.Alquiler;
import com.proyecto.alquiler_vehiculos.entity.EstadoAlquiler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlquilerRepository extends JpaRepository<Alquiler, Long> {

    // Filtra alquileres por estado: ACTIVO, FINALIZADO, CANCELADO
    Page<Alquiler> findByEstado(EstadoAlquiler estado, Pageable pageable);

    // ¿El vehículo ya tiene un alquiler ACTIVO?
    // Regla de negocio: no se puede alquilar un vehículo ya alquilado
    boolean existsByVehiculoIdAndEstado(Long vehiculoId, EstadoAlquiler estado);

    // Búsqueda por nombre del cliente o placa del vehículo
    // JOIN une las 3 tablas para poder filtrar por campos relacionados
    @Query("SELECT a FROM Alquiler a JOIN a.cliente c JOIN a.vehiculo v WHERE " +
            "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(v.placa) LIKE LOWER(CONCAT('%', :q, '%'))")
    Page<Alquiler> buscar(@Param("q") String q, Pageable pageable);
}