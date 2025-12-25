package com.example.demo.service;

import com.example.demo.dto.RateLimitEnforcementDto;
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
import java.util.stream.Collectors;

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
    public RateLimitEnforcementDto createEnforcement(RateLimitEnforcementDto dto) {

        if (dto.getApiKeyId() == null) {
            throw new BadRequestException("apiKeyId is required");
        }

        if (dto.getLimitExceededBy() == null || dto.getLimitExceededBy() < 1) {
            throw new BadRequestException("limitExceededBy must be >= 1");
        }

        ApiKey apiKey = apiKeyRepository.findById(dto.getApiKeyId())
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found"));

        if (!apiKey.isActive()) {
            throw new BadRequestException("Cannot enforce on inactive API key");
        }

        RateLimitEnforcement enforcement = new RateLimitEnforcement();
        enforcement.setApiKey(apiKey);
        enforcement.setBlockedAt(dto.getBlockedAt() != null ? dto.getBlockedAt() : Instant.now());
        enforcement.setLimitExceededBy(dto.getLimitExceededBy());
        enforcement.setMessage(dto.getMessage() != null ? dto.getMessage() : "Rate limit exceeded");

        RateLimitEnforcement saved = enforcementRepository.save(enforcement);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public RateLimitEnforcementDto getEnforcementById(Long id) {
        RateLimitEnforcement e = enforcementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enforcement not found"));
        return toDto(e);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RateLimitEnforcementDto> getEnforcementsForKey(Long apiKeyId) {
        return enforcementRepository.findByApiKeyId(apiKeyId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private RateLimitEnforcementDto toDto(RateLimitEnforcement e) {
        RateLimitEnforcementDto dto = new RateLimitEnforcementDto();
        dto.setId(e.getId());
        dto.setApiKeyId(e.getApiKey().getId());
        dto.setBlockedAt(e.getBlockedAt());
        dto.setLimitExceededBy(e.getLimitExceededBy());
        dto.setMessage(e.getMessage());
        return dto;
    }
}