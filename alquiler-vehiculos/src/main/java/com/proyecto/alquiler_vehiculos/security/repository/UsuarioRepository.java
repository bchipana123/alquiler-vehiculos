package com.proyecto.alquiler_vehiculos.security.repository;

import com.proyecto.alquiler_vehiculos.security.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Spring Security necesita buscar el usuario por username
    Optional<Usuario> findByUsername(String username);
}