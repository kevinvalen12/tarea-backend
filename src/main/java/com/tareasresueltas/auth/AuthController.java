package com.tareasresueltas.auth;

import com.tareasresueltas.auth.dto.AuthResponse;
import com.tareasresueltas.auth.dto.LoginRequest;
import com.tareasresueltas.auth.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
// activa las validaciones de anotaciones
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
// representa al usuario autenticado en el request actual. Spring la inyecta si la pides como parámetro 
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor 
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            // @Valid activa las validaciones
            // @RequestBody toma el JSON del cuerpo y lo comvierte a RegisterRequest
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            // @Valid activa las validaciones
            // @RequestBody toma el JSON del cuerpo y lo comvierte a RegisterRequest
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));

    }

}
