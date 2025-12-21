package com.example.demo.service;

import com.example.demo.dto.ApiKeyRequestDto;
import com.example.demo.dto.ApiKeyUpdateDto;
import com.example.demo.entity.ApiKey;

import java.util.List;

public interface ApiKeyService {
    ApiKey create(ApiKeyRequestDto dto);
    ApiKey update(Long id, ApiKeyUpdateDto dto);
    ApiKey getById(Long id);
    List<ApiKey> getAll();
    ApiKey deactivate(Long id);
}