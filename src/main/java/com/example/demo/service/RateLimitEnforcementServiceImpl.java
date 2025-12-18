package com.example.demo.service;

import com.example.demo.entity.ApiKey;
import com.example.demo.entity.RateLimitEnforcement;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.RateLimitEnforcementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RateLimitEnforcementServiceImpl implements RateLimitEnforcementService {

    private final RateLimitEnforcementRepository rateLimitEnforcementRepository;
    private final ApiKeyRepository apiKeyRepository;

    public RateLimitEnforcementServiceImpl(RateLimitEnforcementRepository rateLimitEnforcementRepository,
                                          ApiKeyRepository apiKeyRepository) {
        this.rateLimitEnforcementRepository = rateLimitEnforcementRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public RateLimitEnforcement createEnforcement(RateLimitEnforcement enforcement) {
        if (enforcement.getLimitExceededBy() == null || enforcement.getLimitExceededBy() < 1) {
            throw new RuntimeException("BadRequest: limitExceededBy must be >= 1");
        }
        if (enforcement.getApiKey() == null || enforcement.getApiKey().getId() == null) {
            throw new RuntimeException("BadRequest: apiKey is required");
        }
        ApiKey key = apiKeyRepository.findById(enforcement.getApiKey().getId())
                .orElseThrow(() -> new RuntimeException("ResourceNotFound: api key not found"));

        enforcement.setApiKey(key);
        return rateLimitEnforcementRepository.save(enforcement);
    }

    @Override
    public RateLimitEnforcement getEnforcementById(Long id) {
        return rateLimitEnforcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ResourceNotFound: enforcement not found"));
    }

    @Override
    public List<RateLimitEnforcement> getEnforcementsForKey(Long keyId) {
        return rateLimitEnforcementRepository.findByApiKey_Id(keyId);
    }
}