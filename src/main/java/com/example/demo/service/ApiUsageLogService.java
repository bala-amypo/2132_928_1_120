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
public class ApiUsageLogService {

    private final ApiUsageLogRepository apiUsageLogRepository;
    private final ApiKeyRepository apiKeyRepository;

    public ApiUsageLogService(ApiUsageLogRepository apiUsageLogRepository,
                              ApiKeyRepository apiKeyRepository) {
        this.apiUsageLogRepository = apiUsageLogRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    // ✅ user sends apiKeyId + endpoint, backend sets timestamp + loads ApiKey
    public ApiUsageLog logUsage(Long apiKeyId, String endpoint) {
        ApiKey apiKey = apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found: " + apiKeyId));

        ApiUsageLog log = new ApiUsageLog();
        log.setApiKey(apiKey);
        log.setEndpoint(endpoint);
        log.setTimestamp(Instant.now()); // ✅ always present time (not future)

        return apiUsageLogRepository.save(log);
    }

    public List<ApiUsageLog> getUsageForApiKey(Long keyId) {
        return apiUsageLogRepository.findByApiKey_IdOrderByTimestampDesc(keyId);
    }

    public List<ApiUsageLog> getUsageForToday(Long keyId) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zoneId);

        Instant start = today.atStartOfDay(zoneId).toInstant();
        Instant end = today.plusDays(1).atStartOfDay(zoneId).toInstant();

        return apiUsageLogRepository.findByApiKey_IdAndTimestampBetweenOrderByTimestampDesc(keyId, start, end);
    }

    public long countRequestsToday(Long keyId) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zoneId);

        Instant start = today.atStartOfDay(zoneId).toInstant();
        Instant end = today.plusDays(1).atStartOfDay(zoneId).toInstant();

        return apiUsageLogRepository.countByApiKey_IdAndTimestampBetween(keyId, start, end);
    }
}