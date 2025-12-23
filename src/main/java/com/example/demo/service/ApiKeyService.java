package com.example.demo.service;

import com.example.demo.dto.ApiKeyRequestDto;
import com.example.demo.dto.ApiKeyResponseDto;

import java.util.List;

public interface ApiKeyService {

    ApiKeyResponseDto create(ApiKeyRequestDto dto);

    ApiKeyResponseDto getById(Long id);

    // ✅ NEW: HQL fetch by keyValue
    ApiKeyResponseDto getByKeyValue(String keyValue);

    List<ApiKeyResponseDto> getAll();

    // ✅ NEW: HQL fetch only active keys
    List<ApiKeyResponseDto> getAllActive();

    ApiKeyResponseDto update(Long id, ApiKeyRequestDto dto);

    ApiKeyResponseDto deactivate(Long id);
}