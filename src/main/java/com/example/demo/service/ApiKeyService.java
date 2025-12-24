package com.example.demo.service;

import com.example.demo.dto.ApiKeyDto;

import java.util.List;

public interface ApiKeyService {
    ApiKeyDto createApiKey(ApiKeyDto dto);
    ApiKeyDto updateApiKey(Long id, ApiKeyDto dto);
    ApiKeyDto getApiKeyById(Long id);
    ApiKeyDto getApiKeyByValue(String keyValue);
    List<ApiKeyDto> getAllApiKeys();
    void deactivateApiKey(Long id);
}