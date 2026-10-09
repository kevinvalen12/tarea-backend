package com.tareasresueltas.shared.config;

import com.tareasresueltas.auth.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// sirve para configurar la seguridad HTTP
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
// implementación del encoder de contraseñas con BCrypt
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;
// configurar los cors
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor 
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    // devuelve la cadena de filtros de seguridad. Spring Security la usará automáticamente.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Deshabilita CSRF. CSRF protege contra ataques donde un sitio malicioso hace 
        // peticiones en nombre del usuario. No aplica a APIs REST stateless con JWT (porque no usamos cookies), 
        // así que lo desactivamos.
        http
            .csrf(AbstractHttpConfigurer::disable)
            // permite que permite frontend en otro dominio
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                //regla de acceso
                //publica register y login
                // las demas necesitan autenticacione
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            // no crear sesiones HTTP cada request es independiente; la "sesión" la lleva el JWT. 
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        // Dominios permitidos
        config.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:4201"));

        // Métodos HTTP permitidos
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        //Cualquier header (incluye Authorization)
        config.setAllowedHeaders(List.of("*"));

        // Permite enviar cookies
        config.setAllowCredentials(true);

        // Aplica esta config a todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;

    }
}
