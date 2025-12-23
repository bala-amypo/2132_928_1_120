package com.example.demo.service;

import com.example.demo.dto.RateLimitEnforcementRequestDto;
import com.example.demo.entity.ApiKey;
import com.example.demo.entity.RateLimitEnforcement;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.RateLimitEnforcementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class RateLimitEnforcementServiceImpl
        implements RateLimitEnforcementService {

    private final RateLimitEnforcementRepository enforcementRepo;
    private final ApiKeyRepository apiKeyRepo;

    public RateLimitEnforcementServiceImpl(
            RateLimitEnforcementRepository enforcementRepo,
            ApiKeyRepository apiKeyRepo) {
        this.enforcementRepo = enforcementRepo;
        this.apiKeyRepo = apiKeyRepo;
    }

    @Override
    @Transactional
    public RateLimitEnforcement create(RateLimitEnforcementRequestDto dto) {

        ApiKey apiKey = apiKeyRepo.findById(dto.getApiKeyId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "ApiKey not found with id: " + dto.getApiKeyId()
                        )
                );

        RateLimitEnforcement e = new RateLimitEnforcement();
        e.setApiKey(apiKey);
        e.setBlockedAt(
                dto.getBlockedAt() == null ? Instant.now() : dto.getBlockedAt()
        );
        e.setLimitExceededBy(dto.getLimitExceededBy());
        e.setMessage(dto.getMessage());

        // ✅ SAVE FIRST
        RateLimitEnforcement saved = enforcementRepo.save(e);

        // ✅ CONDITION AFTER SAVE (rollback demo)
        if (saved.getLimitExceededBy() != null
                && saved.getLimitExceededBy() > 100) {
            throw new ResourceNotFoundException("Testing");
        }

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public RateLimitEnforcement getById(Long id) {
        return enforcementRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "RateLimitEnforcement not found with id: " + id
                        )
                );
    }

    @Override
    @Transactional(readOnly = true)
    public List<RateLimitEnforcement> getByApiKey(Long apiKeyId) {
        return enforcementRepo.findByApiKeyHql(apiKeyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RateLimitEnforcement> getByApiKeyBetween(
            Long apiKeyId, Instant from, Instant to) {

        return enforcementRepo.findByApiKeyBetweenHql(
                apiKeyId, from, to
        );
    }
}