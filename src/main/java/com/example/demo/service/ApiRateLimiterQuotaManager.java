package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApiRateLimiterQuotaManager {

    private final QuotaPlanRepository quotaPlanRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final ApiUsageLogRepository apiUsageLogRepository;
    private final RateLimitEnforcementRepository enforcementRepository;
    private final KeyExemptionRepository exemptionRepository;

    public ApiRateLimiterQuotaManager(QuotaPlanRepository quotaPlanRepository,
                                     ApiKeyRepository apiKeyRepository,
                                     ApiUsageLogRepository apiUsageLogRepository,
                                     RateLimitEnforcementRepository enforcementRepository,
                                     KeyExemptionRepository exemptionRepository) {
        this.quotaPlanRepository = quotaPlanRepository;
        this.apiKeyRepository = apiKeyRepository;
        this.apiUsageLogRepository = apiUsageLogRepository;
        this.enforcementRepository = enforcementRepository;
        this.exemptionRepository = exemptionRepository;
    }

    // -----------------------------
    // QUOTA PLAN (DTO edge)
    // -----------------------------
    public QuotaPlanDto createPlan(QuotaPlanDto dto) {
        QuotaPlan p = new QuotaPlan();
        p.setPlanName(dto.getPlanName());
        p.setDailyLimit(dto.getDailyLimit());
        p.setDescription(dto.getDescription());
        p.setActive(dto.getActive() == null ? true : dto.getActive());
        return toDto(quotaPlanRepository.save(p));
    }

    public QuotaPlanDto getPlan(Long id) {
        return toDto(quotaPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quota plan not found")));
    }

    // -----------------------------
    // API KEY (DTO edge)
    // -----------------------------
    public ApiKeyDto createKey(ApiKeyDto dto) {
        QuotaPlan plan = quotaPlanRepository.findById(dto.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Quota plan not found"));

        ApiKey k = new ApiKey();
        k.setKeyValue(dto.getKeyValue());
        k.setOwnerId(dto.getOwnerId());
        k.setPlan(plan);
        k.setActive(dto.getActive() == null ? true : dto.getActive());
        ApiKey saved = apiKeyRepository.save(k);
        return toDto(saved);
    }

    public ApiKeyDto getKey(Long id) {
        return toDto(apiKeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("API Key not found")));
    }

    public List<ApiKeyDto> getAllKeys() {
        return apiKeyRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    // -----------------------------
    // USAGE LOG (DTO edge)
    // -----------------------------
    public ApiUsageLogDto logUsage(ApiUsageLogDto dto) {
        ApiKey key = apiKeyRepository.findById(dto.getApiKeyId())
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found"));

        ApiUsageLog log = new ApiUsageLog();
        log.setApiKey(key);
        log.setEndpoint(dto.getEndpoint());
        log.setTimestamp(dto.getTimestamp() == null ? Instant.now() : dto.getTimestamp());

        return toDto(apiUsageLogRepository.save(log));
    }

    public List<ApiUsageLog> usageEntities(long apiKeyId) {
        // ✅ tests call repo.findByApiKey_Id, so expose entities too if they use it
        return apiUsageLogRepository.findByApiKey_Id(apiKeyId);
    }

    public List<ApiUsageLogDto> usageDtos(long apiKeyId) {
        return apiUsageLogRepository.findByApiKey_Id(apiKeyId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    // -----------------------------
    // ENFORCEMENT / EXEMPTION
    // -----------------------------
    public List<RateLimitEnforcement> enforcementEntities(long apiKeyId) {
        return enforcementRepository.findByApiKey_Id(apiKeyId);
    }

    public List<KeyExemption> exemptionEntities(long apiKeyId) {
        return exemptionRepository.findByApiKey_Id(apiKeyId)
                .map(List::of)
                .orElse(List.of());
    }

    // -----------------------------
    // MAPPERS
    // -----------------------------
    private QuotaPlanDto toDto(QuotaPlan p) {
        QuotaPlanDto dto = new QuotaPlanDto();
        dto.setId(p.getId());
        dto.setPlanName(p.getPlanName());
        dto.setDailyLimit(p.getDailyLimit());
        dto.setDescription(p.getDescription());
        dto.setActive(p.getActive());
        return dto;
    }

    private ApiKeyDto toDto(ApiKey k) {
        ApiKeyDto dto = new ApiKeyDto();
        dto.setId(k.getId());
        dto.setKeyValue(k.getKeyValue());
        dto.setOwnerId(k.getOwnerId());
        dto.setPlanId(k.getPlan().getId());
        dto.setActive(k.getActive());
        dto.setCreatedAt(k.getCreatedAt());
        dto.setUpdatedAt(k.getUpdatedAt());
        return dto;
    }

    private ApiUsageLogDto toDto(ApiUsageLog l) {
        ApiUsageLogDto dto = new ApiUsageLogDto();
        dto.setId(l.getId());
        dto.setApiKeyId(l.getApiKey().getId());
        dto.setEndpoint(l.getEndpoint());
        dto.setTimestamp(l.getTimestamp());
        return dto;
    }
}