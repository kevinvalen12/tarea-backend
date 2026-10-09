package com.tareasresueltas.shared.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
    // se gaurda el codigo HTTP que queremos deovolver
    private HttpStatus status;
    
    public ApiException(HttpStatus status, String message) {
        // guarda el mensaje en la clase padre para que getMessage() funcione
        super(message);
        this.status = status;
    }
}
