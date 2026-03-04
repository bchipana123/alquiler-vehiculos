package com.proyecto.alquiler_vehiculos.security.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    // La contraseña se guarda ENCRIPTADA en la BD, nunca en texto plano
    private String password;

    @Column(nullable = false)
    private String rol; // ADMIN
}