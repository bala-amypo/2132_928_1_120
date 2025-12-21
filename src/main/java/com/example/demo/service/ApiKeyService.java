package com.example.demo.service;

import com.example.demo.dto.ApiKeyRequestDto;
import com.example.demo.dto.ApiKeyResponseDto;

import java.util.List;

public interface ApiKeyService {
    ApiKeyResponseDto create(ApiKeyRequestDto dto);
    ApiKeyResponseDto getById(Long id);
    List<ApiKeyResponseDto> getAll();
    ApiKeyResponseDto update(Long id, ApiKeyRequestDto dto);
    ApiKeyResponseDto deactivate(Long id);
}