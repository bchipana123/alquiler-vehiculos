package com.proyecto.alquiler_vehiculos.repository;

import com.proyecto.alquiler_vehiculos.entity.EstadoVehiculo;
import com.proyecto.alquiler_vehiculos.entity.Vehiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    boolean existsByPlaca(String placa);

    boolean existsByPlacaAndIdNot(String placa, Long id);

    // Filtra vehículos por estado
    Page<Vehiculo> findByEstado(EstadoVehiculo estado, Pageable pageable);

    // Búsqueda por marca, modelo o placa
    @Query("SELECT v FROM Vehiculo v WHERE " +
            "LOWER(v.marca) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(v.modelo) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(v.placa) LIKE LOWER(CONCAT('%', :q, '%'))")
    Page<Vehiculo> buscar(@Param("q") String q, Pageable pageable);
}