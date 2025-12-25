package com.example.demo.service;

import com.example.demo.entity.RateLimitEnforcement;

import java.util.List;

public interface RateLimitEnforcementService {
    RateLimitEnforcement createEnforcement(RateLimitEnforcement e);
    List<RateLimitEnforcement> getEnforcementsForKey(Long apiKeyId);
    RateLimitEnforcement getEnforcementById(Long id);
}