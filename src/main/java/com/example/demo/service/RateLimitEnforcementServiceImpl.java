package com.example.demo.service;

import com.example.demo.entity.ApiKey;
import com.example.demo.entity.RateLimitEnforcement;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.RateLimitEnforcementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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
        if (enforcement == null) throw new BadRequestException("Enforcement is required");
        if (enforcement.getApiKey() == null || enforcement.getApiKey().getId() == null) {
            throw new BadRequestException("ApiKey is required");
        }
        if (enforcement.getLimitExceededBy() == null || enforcement.getLimitExceededBy() < 1) {
            throw new BadRequestException("Limit exceeded must be >= 1");
        }
        if (enforcement.getMessage() == null || enforcement.getMessage().trim().isEmpty()) {
            throw new BadRequestException("Message is required");
        }

        ApiKey apiKey = apiKeyRepository.findById(enforcement.getApiKey().getId())
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found"));

        if (!apiKey.isActive()) {
            throw new BadRequestException("Cannot enforce on inactive API key");
        }

        enforcement.setApiKey(apiKey);
        if (enforcement.getBlockedAt() == null) enforcement.setBlockedAt(Instant.now());
        enforcement.setMessage(enforcement.getMessage().trim());

        return enforcementRepository.save(enforcement);
    }

    @Override
    @Transactional(readOnly = true)
    public RateLimitEnforcement getEnforcementById(Long id) {
        return enforcementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enforcement not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RateLimitEnforcement> getEnforcementsForKey(Long apiKeyId) {
        return enforcementRepository.findByApiKeyId(apiKeyId);
    }
}