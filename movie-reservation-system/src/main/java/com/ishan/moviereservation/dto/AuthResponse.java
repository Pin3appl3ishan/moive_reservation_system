package com.ishan.moviereservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String refreshToken;
    private String username;
    private String email;
    private String role;
    private String message;
    
    public AuthResponse(String token, String refreshToken, String username, String email, String role) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.username = username;
        this.email = email;
        this.role = role;
    }
    
    public AuthResponse(String message) {
        this.message = message;
    }
}

