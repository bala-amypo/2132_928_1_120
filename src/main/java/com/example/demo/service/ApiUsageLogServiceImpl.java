package com.example.demo.service;

import com.example.demo.entity.ApiKey;
import com.example.demo.entity.ApiUsageLog;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.ApiUsageLogRepository;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

@Service
public class ApiUsageLogServiceImpl implements ApiUsageLogService {

    private final ApiUsageLogRepository apiUsageLogRepository;
    private final ApiKeyRepository apiKeyRepository;

    public ApiUsageLogServiceImpl(ApiUsageLogRepository apiUsageLogRepository, ApiKeyRepository apiKeyRepository) {
        this.apiUsageLogRepository = apiUsageLogRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public ApiUsageLog logUsage(ApiUsageLog log) {
        if (log.getApiKey() == null || log.getApiKey().getId() == null) {
            throw new RuntimeException("BadRequest: apiKey is required");
        }
        if (log.getTimestamp() == null) {
            throw new RuntimeException("BadRequest: timestamp is required");
        }
        if (log.getTimestamp().isAfter(Instant.now())) {
            throw new RuntimeException("BadRequest: timestamp cannot be in the future");
        }

        ApiKey key = apiKeyRepository.findById(log.getApiKey().getId())
                .orElseThrow(() -> new RuntimeException("ResourceNotFound: api key not found"));

        log.setApiKey(key);
        return apiUsageLogRepository.save(log);
    }

    @Override
    public List<ApiUsageLog> getUsageForApiKey(Long keyId) {
        return apiUsageLogRepository.findByApiKey_Id(keyId);
    }

    @Override
    public List<ApiUsageLog> getUsageForToday(Long keyId) {
        Instant start = LocalDate.now(ZoneId.systemDefault()).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = start.plus(Duration.ofDays(1));
        return apiUsageLogRepository.findForKeyBetween(keyId, start, end);
    }

    @Override
    public long countRequestsToday(Long keyId) {
        Instant start = LocalDate.now(ZoneId.systemDefault()).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = start.plus(Duration.ofDays(1));
        return apiUsageLogRepository.countForKeyBetween(keyId, start, end);
    }
}