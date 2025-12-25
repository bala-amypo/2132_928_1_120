package com.example.demo.service;

import com.example.demo.dto.RateLimitEnforcementDto;

import java.util.List;

public interface RateLimitEnforcementService {

    // required by your compiler error
    List<RateLimitEnforcementDto> getEnforcementsForKey(Long apiKeyId);

    List<RateLimitEnforcementDto> getAllEnforcements();

    RateLimitEnforcementDto upsertEnforcement(RateLimitEnforcementDto dto);
}