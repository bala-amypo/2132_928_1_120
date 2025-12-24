package com.example.demo.service;

import com.example.demo.dto.AuthRequestDto;
import com.example.demo.dto.AuthResponseDto;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.dto.RegisterResponseDto;

public interface AuthService {

    RegisterResponseDto register(RegisterRequestDto request);

    AuthResponseDto login(AuthRequestDto request);
}