package com.example.demo.service;

import com.example.demo.dto.KeyExemptionDto;

import java.util.List;

public interface KeyExemptionService {

    KeyExemptionDto createExemption(KeyExemptionDto dto);

    KeyExemptionDto updateExemption(Long id, KeyExemptionDto dto);

    KeyExemptionDto getExemptionByKey(Long apiKeyId);

    List<KeyExemptionDto> getAllExemptions();
}