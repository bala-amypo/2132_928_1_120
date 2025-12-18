package com.example.demo.service;

import com.example.demo.entity.ApiKey;

import java.util.List;

public interface ApiKeyService {
    ApiKey createApiKey(ApiKey key);
    ApiKey updateApiKey(Long id, ApiKey key);
    ApiKey getApiKeyById(Long id);
    ApiKey getApiKeyByValue(String keyValue);
    List<ApiKey> getAllApiKeys();
    ApiKey deactivateApiKey(Long id);
}