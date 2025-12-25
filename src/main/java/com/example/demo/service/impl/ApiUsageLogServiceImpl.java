package com.example.demo.service.impl;

import com.example.demo.entity.ApiKey;
import com.example.demo.entity.ApiUsageLog;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.ApiUsageLogRepository;
import com.example.demo.service.ApiUsageLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
@Transactional
public class ApiUsageLogServiceImpl implements ApiUsageLogService {

    private final ApiUsageLogRepository usageRepo;
    private final ApiKeyRepository apiKeyRepo;

    public ApiUsageLogServiceImpl(ApiUsageLogRepository usageRepo, ApiKeyRepository apiKeyRepo) {
        this.usageRepo = usageRepo;
        this.apiKeyRepo = apiKeyRepo;
    }

    @Override
    public ApiUsageLog logUsage(ApiUsageLog log) {
        if (log == null) throw new BadRequestException("Usage log cannot be null");
        if (log.getApiKey() == null || log.getApiKey().getId() == null) {
            throw new BadRequestException("ApiKey is required");
        }

        ApiKey key = apiKeyRepo.findById(log.getApiKey().getId())
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found"));

        Instant ts = log.getTimestamp() == null ? Instant.now() : log.getTimestamp();
        if (ts.isAfter(Instant.now().plusSeconds(1))) {
            throw new BadRequestException("Timestamp cannot be in the future");
        }

        log.setApiKey(key);
        log.setTimestamp(ts);

        return usageRepo.save(log);
    }

    @Override
    public List<ApiUsageLog> getUsageForToday(Long apiKeyId) {
        Instant start = LocalDate.now(ZoneOffset.UTC).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant end = start.plusSeconds(24 * 60 * 60);
        return usageRepo.findForKeyBetween(apiKeyId, start, end);
    }

    @Override
    public int countRequestsToday(Long apiKeyId) {
        Instant start = LocalDate.now(ZoneOffset.UTC).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant end = start.plusSeconds(24 * 60 * 60);
        return usageRepo.countForKeyBetween(apiKeyId, start, end);
    }

    @Override
    public List<ApiUsageLog> getUsageForApiKey(Long apiKeyId) {
        return usageRepo.findByApiKey_Id(apiKeyId);
    }
}