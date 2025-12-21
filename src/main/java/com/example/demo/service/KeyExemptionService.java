// src/main/java/com/example/demo/service/KeyExemptionService.java
package com.example.demo.service;

import com.example.demo.dto.KeyExemptionRequestDto;
import com.example.demo.dto.KeyExemptionResponseDto;

import java.util.List;

public interface KeyExemptionService {
    KeyExemptionResponseDto create(KeyExemptionRequestDto dto);
    KeyExemptionResponseDto update(Long id, KeyExemptionRequestDto dto);
    List<KeyExemptionResponseDto> getAllExemptions();
    List<KeyExemptionResponseDto> getByApiKeyId(Long apiKeyId);
    KeyExemptionResponseDto getById(Long id);
}