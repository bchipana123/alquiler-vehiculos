package com.proyecto.alquiler_vehiculos.modulo_clientes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Table(name = "clientes")
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El DNI es obligatorio")
    @Column(unique = true, nullable = false)
    private String dni;

    @Email(message = "El email no es válido")
    private String email;

    @Pattern(regexp = "9\\d{8}", message = "El teléfono debe empezar con 9 y tener 9 dígitos")
    private String telefono;
}