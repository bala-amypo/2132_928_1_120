package com.example.demo.service;

import com.example.demo.dto.RateLimitEnforcementRequestDto;
import com.example.demo.entity.RateLimitEnforcement;

import java.time.Instant;
import java.util.List;

public interface RateLimitEnforcementService {

    RateLimitEnforcement create(RateLimitEnforcementRequestDto dto);

    RateLimitEnforcement getById(Long id);

    List<RateLimitEnforcement> getByApiKey(Long apiKeyId);

    List<RateLimitEnforcement> getByApiKeyBetween(
            Long apiKeyId, Instant from, Instant to
    );
}