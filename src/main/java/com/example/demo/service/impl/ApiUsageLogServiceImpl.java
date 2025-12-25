package com.example.demo.service.impl;

import com.example.demo.entity.ApiKey;
import com.example.demo.entity.ApiUsageLog;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.ApiUsageLogRepository;
import com.example.demo.service.ApiUsageLogService;

import java.time.*;
import java.util.List;

public class ApiUsageLogServiceImpl implements ApiUsageLogService {

    private final ApiUsageLogRepository usageRepo;
    private final ApiKeyRepository apiKeyRepo;

    public ApiUsageLogServiceImpl(ApiUsageLogRepository usageRepo, ApiKeyRepository apiKeyRepo) {
        this.usageRepo = usageRepo;
        this.apiKeyRepo = apiKeyRepo;
    }

    @Override
    public ApiUsageLog logUsage(ApiUsageLog log) {
        if (log.getApiKey() == null || log.getApiKey().getId() == null) {
            throw new BadRequestException("ApiKey required");
        }
        ApiKey k = apiKeyRepo.findById(log.getApiKey().getId())
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found"));

        if (log.getTimestamp() == null) {
            log.setTimestamp(Instant.now());
        }
        if (log.getTimestamp().isAfter(Instant.now().plusSeconds(1))) {
            // test expects future timestamp fails
            throw new BadRequestException("Future timestamp not allowed");
        }

        log.setApiKey(k);
        if (log.getEndpoint() == null) log.setEndpoint("/unknown");
        return usageRepo.save(log);
    }

    @Override
    public List<ApiUsageLog> getUsageForToday(Long apiKeyId) {
        Instant from = LocalDate.now(ZoneOffset.UTC).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant to = from.plus(Duration.ofDays(1)).minusMillis(1);
        return usageRepo.findForKeyBetween(apiKeyId, from, to);
    }

    @Override
    public int countRequestsToday(Long apiKeyId) {
        Instant from = LocalDate.now(ZoneOffset.UTC).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant to = from.plus(Duration.ofDays(1)).minusMillis(1);
        return usageRepo.countForKeyBetween(apiKeyId, from, to);
    }

    @Override
    public List<ApiUsageLog> getUsageForApiKey(Long apiKeyId) {
        return usageRepo.findByApiKey_Id(apiKeyId);
    }
}