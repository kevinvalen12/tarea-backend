package com.tareasresueltas.auth.jwt;

<<<<<<< HEAD
//asa la petición al siguiente filtro
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
// la petición y respuesta HTTP
import jakarta.servlet.http.HttpServletRequest;
import  jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
//solo documenta que los parametros no deben de ser null

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
//contenedor global de spring boot security guarda "quien esta autenticado ahora"
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
// filtro que Spring garantiza que se ejecute una sola vez por petición, sin importar redirecciones internas.
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import jakarta.validation.constraints.NotNull;

// se registra este filtro como un beat para que spring boot lo pueda utilizar
@Component
// hereda la logica "solo "ejecuta una vez por request"
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, 
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws  ServletException, IOException {
        //Extrae el header Authorization de la petición.
        final String authHeader = request.getHeader("Authorization");

        //Si no hay token → dejamos pasar la petición sin autenticar y login decide si lo deja pasar o no
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // pasa la peticion al siguiente filtro
            filterChain.doFilter(request, response);
            return;
        }

        //coarta los primeros caracteres "Bearer " y solo deja el token
        String token = authHeader.substring(7);

        // lanza la execepcion si el token es valido o esta corrupto
        try {
            String email = jwtService.extractEmail(token);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if(jwtService.isTokenvalid(token, email)) {
                    var authToken = new UsernamePasswordAuthenticationToken(
                        email,
                        //simepre en null porque ya lo validamos con el token y no se necesita guardar la contraseña 
                        null, 
                        // roles y permisos
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );

                    // Agrega detalles de la peticion (ip, sessionid) al objeto autenticacion 
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // aca se le dice a spring secury "en este hilo, el usuario autenticado es este"
                    // a partir de ahora, dentra al controller
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
        }

        filterChain.doFilter(request, response);
    }
=======
public class JwtAuthFilter {

>>>>>>> f3207bebf6807f491a851efb68451f5b1ecbd1f3
}
