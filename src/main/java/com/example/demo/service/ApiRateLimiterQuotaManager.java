package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ApiRateLimiterQuotaManager {

    private final ApiKeyRepository apiKeyRepository;
    private final QuotaPlanRepository quotaPlanRepository;
    private final ApiUsageLogRepository apiUsageLogRepository;
    private final RateLimitEnforcementRepository enforcementRepository;
    private final KeyExemptionRepository exemptionRepository;

    public ApiRateLimiterQuotaManager(ApiKeyRepository apiKeyRepository,
                                      QuotaPlanRepository quotaPlanRepository,
                                      ApiUsageLogRepository apiUsageLogRepository,
                                      RateLimitEnforcementRepository enforcementRepository,
                                      KeyExemptionRepository exemptionRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.quotaPlanRepository = quotaPlanRepository;
        this.apiUsageLogRepository = apiUsageLogRepository;
        this.enforcementRepository = enforcementRepository;
        this.exemptionRepository = exemptionRepository;
    }

    public boolean isKeyExempted(Long apiKeyId) {
        List<KeyExemption> exemptions = exemptionRepository.findByApiKey_Id(apiKeyId);
        return exemptions.stream().anyMatch(KeyExemption::isExempted);
    }

    public Optional<RateLimitEnforcement> getEnforcement(Long apiKeyId) {
        return enforcementRepository.findByApiKey_Id(apiKeyId);
    }

    public List<ApiUsageLog> getUsageLogs(Long apiKeyId) {
        return apiUsageLogRepository.findByApiKey_Id(apiKeyId);
    }

    public boolean isBlockedNow(Long apiKeyId) {
        Optional<RateLimitEnforcement> opt = enforcementRepository.findByApiKey_Id(apiKeyId);
        if (!opt.isPresent()) return false;

        RateLimitEnforcement e = opt.get();
        if (!e.isBlocked()) return false;

        Instant until = e.getBlockedUntil();
        return until != null && until.isAfter(Instant.now());
    }
}