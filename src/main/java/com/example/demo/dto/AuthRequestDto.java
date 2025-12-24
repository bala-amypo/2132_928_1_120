package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthRequestDto {

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    // 🔹 No-args constructor (required by Jackson)
    public AuthRequestDto() {
    }

    // 🔹 All-args constructor (optional but useful)
    public AuthRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // 🔹 Getters & Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}