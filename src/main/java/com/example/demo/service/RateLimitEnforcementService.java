package com.example.demo.service;

import com.example.demo.dto.RateLimitEnforcementDto;

import java.util.List;

public interface RateLimitEnforcementService {

    RateLimitEnforcementDto createEnforcement(RateLimitEnforcementDto dto);

    RateLimitEnforcementDto getEnforcementById(Long id);

    List<RateLimitEnforcementDto> getEnforcementsForKey(Long apiKeyId);
}