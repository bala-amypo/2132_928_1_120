package com.example.demo.service;

import com.example.demo.dto.AuthRequestDto;
import com.example.demo.dto.AuthResponseDto;
import com.example.demo.dto.RegisterRequestDto;

public interface AuthService {
    void register(RegisterRequestDto dto);
    AuthResponseDto login(AuthRequestDto dto);
}