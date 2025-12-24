package com.example.demo.dto;

public class AuthResponseDto {

    private final Long userId;
    private final String email;
    private final String role;
    private final String token;

    public AuthResponseDto(Long userId, String email, String role, String token) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.token = token;
    }

    public Long getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getToken() { return token; }
}