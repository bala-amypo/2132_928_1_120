package com.example.demo.service;

import com.example.demo.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiRateLimiterQuotaManager {

    private final QuotaPlanService quotaPlanService;
    private final ApiKeyService apiKeyService;
    private final ApiUsageLogService apiUsageLogService;
    private final RateLimitEnforcementService rateLimitEnforcementService;
    private final KeyExemptionService keyExemptionService;
    private final AuthService authService;

    public ApiRateLimiterQuotaManager(QuotaPlanService quotaPlanService,
                                      ApiKeyService apiKeyService,
                                      ApiUsageLogService apiUsageLogService,
                                      RateLimitEnforcementService rateLimitEnforcementService,
                                      KeyExemptionService keyExemptionService,
                                      AuthService authService) {
        this.quotaPlanService = quotaPlanService;
        this.apiKeyService = apiKeyService;
        this.apiUsageLogService = apiUsageLogService;
        this.rateLimitEnforcementService = rateLimitEnforcementService;
        this.keyExemptionService = keyExemptionService;
        this.authService = authService;
    }

    // ======================
    // USER
    // ======================
    public RegisterResponseDto register(RegisterRequestDto request) {
        return authService.register(request);
    }

    public AuthResponseDto login(AuthRequestDto request) {
        return authService.login(request);
    }

    // ======================
    // QUOTA PLAN (DTO)
    // ======================
    public QuotaPlanDto createQuotaPlan(QuotaPlanDto dto) {
        return quotaPlanService.createQuotaPlan(dto);
    }

    public QuotaPlanDto updateQuotaPlan(Long id, QuotaPlanDto dto) {
        return quotaPlanService.updateQuotaPlan(id, dto);
    }

    public QuotaPlanDto getQuotaPlan(Long id) {
        return quotaPlanService.getQuotaPlanById(id);
    }

    public List<QuotaPlanDto> getAllPlans() {
        return quotaPlanService.getAllPlans();
    }

    public void deactivateQuotaPlan(Long id) {
        quotaPlanService.deactivateQuotaPlan(id);
    }

    // ======================
    // API KEY (DTO)
    // ======================
    public ApiKeyDto createApiKey(ApiKeyDto dto) {
        return apiKeyService.createApiKey(dto);
    }

    public ApiKeyDto updateApiKey(Long id, ApiKeyDto dto) {
        return apiKeyService.updateApiKey(id, dto);
    }

    public ApiKeyDto getApiKey(Long id) {
        return apiKeyService.getApiKeyById(id);
    }

    public List<ApiKeyDto> getAllApiKeys() {
        return apiKeyService.getAllApiKeys();
    }

    public void deactivateApiKey(Long id) {
        apiKeyService.deactivateApiKey(id);
    }

    // ======================
    // USAGE LOG (DTO)
    // ======================
    public ApiUsageLogDto logUsage(ApiUsageLogDto dto) {
        return apiUsageLogService.logUsage(dto);
    }

    public List<ApiUsageLogDto> usageForKey(Long keyId) {
        return apiUsageLogService.getUsageForApiKey(keyId);
    }

    public int countRequestsToday(Long keyId) {
        return apiUsageLogService.countRequestsToday(keyId);
    }

    // ======================
    // ENFORCEMENT (DTO)
    // ======================
    public RateLimitEnforcementDto enforce(RateLimitEnforcementDto dto) {
        return rateLimitEnforcementService.createEnforcement(dto);
    }

    public RateLimitEnforcementDto getEnforcement(Long id) {
        return rateLimitEnforcementService.getEnforcementById(id);
    }

    public List<RateLimitEnforcementDto> getEnforcementsForKey(Long apiKeyId) {
        return rateLimitEnforcementService.getEnforcementsForKey(apiKeyId);
    }

    // ======================
    // EXEMPTIONS (DTO)
    // ======================
    public KeyExemptionDto createExemption(KeyExemptionDto dto) {
        return keyExemptionService.createExemption(dto);
    }

    public KeyExemptionDto updateExemption(Long id, KeyExemptionDto dto) {
        return keyExemptionService.updateExemption(id, dto);
    }

    public KeyExemptionDto getExemptionByKey(Long apiKeyId) {
        return keyExemptionService.getExemptionByKey(apiKeyId);
    }

    public List<KeyExemptionDto> getAllExemptions() {
        return keyExemptionService.getAllExemptions();
    }
}