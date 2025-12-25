package com.example.demo.service;

import com.example.demo.dto.RateLimitEnforcementDto;
import com.example.demo.entity.ApiKey;
import com.example.demo.entity.RateLimitEnforcement;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.RateLimitEnforcementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<RateLimitEnforcementDto> getEnforcementsForKey(Long apiKeyId) {
        return enforcementRepository.findByApiKey_Id(apiKeyId)
                .map(e -> List.of(toDto(e)))
                .orElse(List.of());
    }

    @Override
    public List<RateLimitEnforcementDto> getAllEnforcements() {
        return enforcementRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RateLimitEnforcementDto upsertEnforcement(RateLimitEnforcementDto dto) {
        ApiKey apiKey = apiKeyRepository.findById(dto.getApiKeyId())
                .orElseThrow(() -> new ResourceNotFoundException("API key not found: " + dto.getApiKeyId()));

        RateLimitEnforcement e = enforcementRepository.findByApiKey_Id(dto.getApiKeyId())
                .orElseGet(RateLimitEnforcement::new);

        e.setApiKey(apiKey);
        e.setBlocked(dto.isBlocked());
        e.setReason(dto.getReason());
        e.setBlockedUntil(dto.getBlockedUntil());

        RateLimitEnforcement saved = enforcementRepository.save(e);
        return toDto(saved);
    }

    private RateLimitEnforcementDto toDto(RateLimitEnforcement e) {
        RateLimitEnforcementDto dto = new RateLimitEnforcementDto();
        dto.setId(e.getId());
        dto.setApiKeyId(e.getApiKey() != null ? e.getApiKey().getId() : null);
        dto.setBlocked(e.isBlocked());
        dto.setReason(e.getReason());
        dto.setBlockedUntil(e.getBlockedUntil());
        return dto;
    }
}