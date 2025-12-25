package com.example.demo.service;

import com.example.demo.entity.KeyExemption;
import com.example.demo.entity.RateLimitEnforcement;
import com.example.demo.repository.ApiUsageLogRepository;
import com.example.demo.repository.KeyExemptionRepository;
import com.example.demo.repository.RateLimitEnforcementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ApiRateLimiterQuotaManager {

    private final ApiUsageLogRepository apiUsageLogRepository;
    private final RateLimitEnforcementRepository enforcementRepository;
    private final KeyExemptionRepository exemptionRepository;

    public ApiRateLimiterQuotaManager(ApiUsageLogRepository apiUsageLogRepository,
                                      RateLimitEnforcementRepository enforcementRepository,
                                      KeyExemptionRepository exemptionRepository) {
        this.apiUsageLogRepository = apiUsageLogRepository;
        this.enforcementRepository = enforcementRepository;
        this.exemptionRepository = exemptionRepository;
    }

    public boolean isKeyExempted(Long apiKeyId) {
        List<KeyExemption> list = exemptionRepository.findByApiKey_Id(apiKeyId);
        return list.stream().anyMatch(KeyExemption::isExempted);
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