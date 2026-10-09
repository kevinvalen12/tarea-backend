package com.tareasresueltas.user;

// genera getters/setters setters automáticamente
import lombok.Data;
// marca cual campo es el ID de MongoDB
import org.springframework.data.annotation.Id;
// Crea Indeces en MongoDB
import org.springframework.data.mongodb.core.index.Indexed;
// le dice a Spring boot "esta clase es una coleccion en MongoDB"
import org.springframework.data.mongodb.core.mapping.Document;

// Tipo de datos para la fecha
import java.time.LocalDateTime;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;
    @Indexed(unique = true)
    private String email;

    private String firstName;
    private String secondFirstName;

    private String lastName;
    private String secondLastName;

    private String userName;

    private String password;

    private String role = "USER";

    private boolean enabled = true;

    private LocalDateTime createdAt = LocalDateTime.now();
}
