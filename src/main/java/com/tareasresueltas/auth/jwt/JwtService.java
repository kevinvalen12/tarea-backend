package com.tareasresueltas.auth.jwt;

// representa el "payload" del token (los datos)
import io.jsonwebtoken.Claims;
// fábrica de jjwt para construir y parsear tokens
import io.jsonwebtoken.Jwts;
//utilidad para crear la clave de firma
import io.jsonwebtoken.security.Keys;
// inyecta valores desde application.properties
import org.springframework.beans.factory.annotation.Value;
// extraer distintos campos del token
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

// le dice spring boot que este es un servicio y se puede inyectar donde se requiera
@Service 
public class JwtService {

    // spring boot busca la propiedad app.jwt.token application.properties
    // y le asigna el valor a secret porque no se asignaria null 
    @Value("${app.jwt.secret}")
    private String secret;

    // es el tiempo de vida del token
    @Value("${app.jwt.expiration}")
    private long expiration;

    /**
     * Jwts.builder() = construye el token
     * .subject(email) = pone el email en el campo sud en el payload forma estandar para identifica el dueño deñ token
     * issuedAt(new Date()) = marca la fecha de creacion y este no acepta token viejos
     * .expiration(...) = pone la fecha de expiracion
     * signWith(getSigningKey()) = firma el token con la clave secreta aun asi cualquiera puede falsificar el tocken 
     * .compact() = convierte todo a la cadena final xxxxx.yyyyy.zzzzz
     * @param email
     * @return devuleve un String que es el token
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /*
    parsea el token aplica las funciiones para extraer los campos  
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /*
    Valeida dos cosa que el email dentro del token sea el espera y que token no haya expirado
    */
    public boolean isTokenvalid(String token, String email) {
        final String tokenEmail = extractEmail(token);
        return tokenEmail.equals(email) && !isTokenExpired(token);
    }

    /**
     * extra la fecha de expiracion y la compara
     * @param token
     * @return true si el token expiro
     */
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /*
    <T> T = es genérico. Devuelve un tipo T que se decide en la llamada.
    Function<Claims, T> resolver = recibe una función que toma Claims y devuelve T.
    Jwts.parser().verifyWith(...).build() = crea un parser que verificará la firma con nuestra clave.
    .parseSignedClaims(token) = valida la firma y devuelve los claims. Si la firma es inválida, lanza excepción (JwtException).
    .getPayload() = obtiene los datos del payload (Claims).
    resolver.apply(claims) = aplica la función que le pasamos. Si pasamos Claims::getSubject, devuelve el email. Si pasamos Claims::getExpiration, devuelve la fecha de expiración.
    */
    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return  resolver.apply(claims);
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

}
