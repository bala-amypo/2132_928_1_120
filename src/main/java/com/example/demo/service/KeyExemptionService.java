package com.example.demo.service;

import com.example.demo.dto.KeyExemptionDto;

import java.util.List;

public interface KeyExemptionService {

    // required by your compiler error
    List<KeyExemptionDto> getAllExemptions();

    // per-key fetch
    List<KeyExemptionDto> getExemptionsForKey(Long apiKeyId);

    // create/update
    KeyExemptionDto upsertExemption(KeyExemptionDto dto);
}