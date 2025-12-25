package com.example.demo.service.impl;

import com.example.demo.entity.ApiKey;
import com.example.demo.entity.RateLimitEnforcement;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.RateLimitEnforcementRepository;
import com.example.demo.service.RateLimitEnforcementService;

import java.util.List;

public class RateLimitEnforcementServiceImpl implements RateLimitEnforcementService {

    private final RateLimitEnforcementRepository enforceRepo;
    private final ApiKeyRepository apiKeyRepo;

    public RateLimitEnforcementServiceImpl(RateLimitEnforcementRepository enforceRepo, ApiKeyRepository apiKeyRepo) {
        this.enforceRepo = enforceRepo;
        this.apiKeyRepo = apiKeyRepo;
    }

    @Override
    public RateLimitEnforcement createEnforcement(RateLimitEnforcement e) {
        if (e.getApiKey() == null || e.getApiKey().getId() == null) {
            throw new BadRequestException("ApiKey required");
        }
        ApiKey k = apiKeyRepo.findById(e.getApiKey().getId())
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found"));

        if (e.getLimitExceededBy() <= 0) {
            // test expects 0 fails
            throw new BadRequestException("limitExceededBy must be > 0");
        }

        e.setApiKey(k);
        return enforceRepo.save(e);
    }

    @Override
    public List<RateLimitEnforcement> getEnforcementsForKey(Long apiKeyId) {
        return enforceRepo.findByApiKey_Id(apiKeyId);
    }

    @Override
    public RateLimitEnforcement getEnforcementById(Long id) {
        return enforceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enforcement not found"));
    }
}