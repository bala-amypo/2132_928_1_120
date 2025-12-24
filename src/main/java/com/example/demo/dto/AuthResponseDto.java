package com.example.demo.dto;

public class AuthResponseDto {
    private Long userId;
    private String email;
    private String token;
    private String role;

    public AuthResponseDto(Long userId, String email, String token, String role) {
        this.userId = userId;
        this.email = email;
        this.token = token;
        this.role = role;
    }

    public Long getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getToken() { return token; }
    public String getRole() { return role; }
}