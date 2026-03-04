package com.proyecto.alquiler_vehiculos.controller;

import com.proyecto.alquiler_vehiculos.security.JwtService;
import com.proyecto.alquiler_vehiculos.security.dto.LoginRequestDTO;
import com.proyecto.alquiler_vehiculos.security.dto.LoginResponseDTO;
import com.proyecto.alquiler_vehiculos.security.entity.Usuario;
import com.proyecto.alquiler_vehiculos.security.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserDetailsService userDetailsService,
                          UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService             = jwtService;
        this.userDetailsService     = userDetailsService;
        this.usuarioRepository      = usuarioRepository;
        this.passwordEncoder        = passwordEncoder;
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request) {

        // Verifica usuario y contraseña contra la BD
        // Si son incorrectos lanza BadCredentialsException → 401
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(), request.password()));

        // Si llegó aquí las credenciales son correctas
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.username());

        // Genera el token JWT
        String token = jwtService.generarToken(userDetails);

        // Busca el usuario para devolver el rol
        Usuario usuario = usuarioRepository
                .findByUsername(request.username()).orElseThrow();

        return ResponseEntity.ok(new LoginResponseDTO(
                token,
                usuario.getUsername(),
                usuario.getRol()
        ));
    }

    // POST /api/auth/registro
    // Útil para crear el usuario admin inicial
    @PostMapping("/registro")
    public ResponseEntity<String> registro(
            @Valid @RequestBody LoginRequestDTO request) {

        if (usuarioRepository.findByUsername(request.username()).isPresent())
            return ResponseEntity.badRequest()
                    .body("El usuario ya existe");

        Usuario usuario = new Usuario();
        usuario.setUsername(request.username());
        // Encripta la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setRol("ADMIN");
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuario creado correctamente");
    }
}