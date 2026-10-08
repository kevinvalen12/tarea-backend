package com.tareasresueltas.auth;

import com.tareasresueltas.auth.dto.AuthResponse;
import com.tareasresueltas.auth.dto.LoginRequest;
import com.tareasresueltas.auth.dto.RegisterRequest;
import com.tareasresueltas.auth.jwt.JwtService;
import com.tareasresueltas.shared.exception.ApiException;
import com.tareasresueltas.user.User;
import com.tareasresueltas.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    //
    public AuthResponse register(RegisterRequest request) {
        // existsByEmail → consulta a Mongo
        // si existe lanza uan ApiException con el estado 409 Conflict
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(HttpStatus.CONFLICT, "The email address is already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setSecondFirstName(request.getSecondFirstName());
        user.setLastName(request.getLastName());
        user.setSecondLastName(request.getSecondLastName());
        user.setUserName(request.getUserName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Inserta el usuario en mongo
        userRepository.save(user);

        // Genera el JWT para que el usuario no tenga necesidad de Loguearse depeues de
        // registrarse
        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token, "Invalid credentials");
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token, "Invalid credentials");
    }
}
