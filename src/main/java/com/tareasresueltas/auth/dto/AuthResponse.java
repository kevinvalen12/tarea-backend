package com.tareasresueltas.auth.dto;


public class AuthResponse {
    private String token;

    public AuthResponse(String token,) {
        this.token = token;
    }

    // + getter y setters
    public String gatToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    } 
}
