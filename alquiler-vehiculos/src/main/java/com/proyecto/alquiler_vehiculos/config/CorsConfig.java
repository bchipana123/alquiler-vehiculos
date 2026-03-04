package com.proyecto.alquiler_vehiculos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// CORS = Cross-Origin Resource Sharing
// El navegador bloquea peticiones entre dominios distintos.
// Angular corre en localhost:4200 y Spring en localhost:8080.
// Sin esta config, Angular no podría comunicarse con el backend.
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);

        // Solo aceptamos peticiones del frontend Angular
        config.addAllowedOrigin("http://localhost:4200");

        config.addAllowedHeader("*");

        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Aplica esta config a TODOS los endpoints de la API
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}