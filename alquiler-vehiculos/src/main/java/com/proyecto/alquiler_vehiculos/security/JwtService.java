package com.proyecto.alquiler_vehiculos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

// Se encarga de GENERAR y VALIDAR los tokens JWT
@Service
public class JwtService {

    // Clave secreta para firmar el token
    // En producción esto iría en application.properties
    private static final String SECRET_KEY =
            "alquilerVehiculosSecretKeyMuySegura2025Galaxy";

    // Tiempo de expiración: 24 horas en milisegundos
    private static final long EXPIRATION = 86400000;

    // Genera la clave criptográfica a partir del string
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // GENERA un token JWT con el username dentro
    public String generarToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getKey())
                .compact();
    }

    // EXTRAE el username del token
    public String extraerUsername(String token) {
        return extraerClaims(token).getSubject();
    }

    // VALIDA que el token sea correcto y no haya expirado
    public boolean validarToken(String token, UserDetails userDetails) {
        String username = extraerUsername(token);
        return username.equals(userDetails.getUsername())
                && !tokenExpirado(token);
    }

    private boolean tokenExpirado(String token) {
        return extraerClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // Parsea el token y extrae toda la información (Claims)
    private Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}