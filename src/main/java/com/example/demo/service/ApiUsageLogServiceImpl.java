package com.example.demo.service;

import com.example.demo.entity.ApiKey;
import com.example.demo.entity.ApiUsageLog;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.ApiUsageLogRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class ApiUsageLogServiceImpl implements ApiUsageLogService {

    private final ApiUsageLogRepository apiUsageLogRepository;
    private final ApiKeyRepository apiKeyRepository;

    public ApiUsageLogServiceImpl(ApiUsageLogRepository apiUsageLogRepository,
                                  ApiKeyRepository apiKeyRepository) {
        this.apiUsageLogRepository = apiUsageLogRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public ApiUsageLog logUsage(Long apiKeyId, String endpoint) {

        ApiKey apiKey = apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("ApiKey not found with id: " + apiKeyId));

        ApiUsageLog log = new ApiUsageLog();
        log.setApiKey(apiKey);           // ✅ REQUIRED
        log.setEndpoint(endpoint);
        log.setTimestamp(Instant.now()); // ✅ Avoid future timestamp issue

        return apiUsageLogRepository.save(log);
    }

    @Override
    public List<ApiUsageLog> getUsageForApiKey(Long keyId) {
        return apiUsageLogRepository
                .findByApiKey_IdOrderByTimestampDesc(keyId);
    }

    @Override
    public List<ApiUsageLog> getUsageForToday(Long keyId) {

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zoneId);

        Instant start = today.atStartOfDay(zoneId).toInstant();
        Instant end = today.plusDays(1).atStartOfDay(zoneId).toInstant();

        return apiUsageLogRepository
                .findByApiKey_IdAndTimestampBetweenOrderByTimestampDesc(
                        keyId, start, end);
    }

    @Override
    public long countRequestsToday(Long keyId) {

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zoneId);

        Instant start = today.atStartOfDay(zoneId).toInstant();
        Instant end = today.plusDays(1).atStartOfDay(zoneId).toInstant();

        return apiUsageLogRepository
                .countByApiKey_IdAndTimestampBetween(keyId, start, end);
    }
}