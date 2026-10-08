package com.tareasresueltas.shared.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice 
public class GlobalExceptionHandler {

    // dice cuando se lance ApiExceptionen cualquier parte, ejecuta eeste metodo
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleApiException(ApiException ex) {
        return buildResponse(ex.getStatus(), ex.getMessage());
    }

    // lanza un spring automaticamente cuando falle @Valid en el controler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) 
    {
        // se construlle el mapa(se realiza en JSON) con la informacion del error 
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());

        // getBindingResult contiene el resulatdo de las validaciones
        // getFieldErrors() lista de errores campo por campo
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        // devuelve un 400 con el body
        return ResponseEntity.badRequest().body(body);
    }

    // Red de seguridad final 
    //Cualquier excepción no controlada cae aquí. Nunca filtres el mensaje real al cliente (podría revelar información sensible). 
    // Registra el ex con logs (no lo hice para no complicar) y devuelve un mensaje genérico.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    // Método helper para no repetir código
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status);
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
