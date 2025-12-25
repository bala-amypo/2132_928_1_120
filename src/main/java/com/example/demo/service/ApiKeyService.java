package com.example.demo.service;

import com.example.demo.dto.ApiKeyDto;

import java.util.List;

public interface ApiKeyService {
    ApiKeyDto createApiKey(ApiKeyDto dto);
    ApiKeyDto getApiKey(Long id);
    List<ApiKeyDto> getAllApiKeys();
    void deleteApiKey(Long id);
}