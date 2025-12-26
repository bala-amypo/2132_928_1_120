package com.example.demo.service.impl;

import com.example.demo.entity.ApiKey;
import com.example.demo.entity.RateLimitEnforcement;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.RateLimitEnforcementRepository;
import com.example.demo.service.RateLimitEnforcementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RateLimitEnforcementServiceImpl implements RateLimitEnforcementService {

    private final RateLimitEnforcementRepository enforcementRepository;
    private final ApiKeyRepository apiKeyRepository;

    public RateLimitEnforcementServiceImpl(RateLimitEnforcementRepository enforcementRepository,
                                          ApiKeyRepository apiKeyRepository) {
        this.enforcementRepository = enforcementRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public RateLimitEnforcement createEnforcement(RateLimitEnforcement enforcement) {
        if (enforcement == null) throw new BadRequestException("Enforcement cannot be null");
        if (enforcement.getApiKey() == null || enforcement.getApiKey().getId() == null)
            throw new BadRequestException("ApiKey is required");

        if (enforcement.getLimitExceededBy() <= 0)
            throw new BadRequestException("limitExceededBy must be > 0");

        // Validate apiKey exists (your tests expect this lookup)
        ApiKey key = apiKeyRepository.findById(enforcement.getApiKey().getId())
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found: " + enforcement.getApiKey().getId()));

        enforcement.setApiKey(key);

        return enforcementRepository.save(enforcement);
    }

    @Override
    public RateLimitEnforcement getEnforcementById(Long id) {
        return enforcementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enforcement not found: " + id));
    }

    @Override
    public List<RateLimitEnforcement> getEnforcementsForKey(Long apiKeyId) {
        return enforcementRepository.findByApiKey_Id(apiKeyId);
    }
}