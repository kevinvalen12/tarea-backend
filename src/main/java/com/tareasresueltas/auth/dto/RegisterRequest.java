package com.tareasresueltas.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "The email address is required")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "The first name is mandatory")
    private String firstName;

    @NotBlank(message = "The first surname is mandatory")
    private String lastName;

    @NotBlank(message = "The user is required.")
    private String userName;

    @NotBlank(message = "The password is required")
    @Size(min = 5, message = "Minimum of 6 characters")
    private String password;


}
