package com.example.demo.service;

import com.example.demo.dto.KeyExemptionRequestDto;
import com.example.demo.entity.KeyExemption;

import java.util.List;

public interface KeyExemptionService {
    KeyExemption createExemption(KeyExemptionRequestDto dto);
    KeyExemption updateExemption(Long id, KeyExemptionRequestDto dto);
    KeyExemption getExemptionByKey(Long apiKeyId);
    List<KeyExemption> getAllExemptions();
}