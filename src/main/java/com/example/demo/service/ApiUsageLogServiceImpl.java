package com.example.demo.service;

import com.example.demo.entity.ApiKey;
import com.example.demo.entity.ApiUsageLog;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.ApiUsageLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@Transactional
public class ApiUsageLogServiceImpl implements ApiUsageLogService {

    private final ApiUsageLogRepository apiUsageLogRepository;
    private final ApiKeyRepository apiKeyRepository;

    public ApiUsageLogServiceImpl(ApiUsageLogRepository apiUsageLogRepository,
                                  ApiKeyRepository apiKeyRepository) {
        this.apiUsageLogRepository = apiUsageLogRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public ApiUsageLog logUsage(ApiUsageLog log) {
        if (log == null) throw new BadRequestException("Log is required");
        if (log.getEndpoint() == null || log.getEndpoint().trim().isEmpty()) {
            throw new BadRequestException("Endpoint is required");
        }
        if (log.getApiKey() == null || log.getApiKey().getId() == null) {
            throw new BadRequestException("ApiKey is required");
        }
        if (log.getTimestamp() != null && log.getTimestamp().isAfter(Instant.now())) {
            throw new BadRequestException("timestamp cannot be in the future");
        }

        ApiKey apiKey = apiKeyRepository.findById(log.getApiKey().getId())
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found"));

        if (!apiKey.isActive()) {
            throw new BadRequestException("ApiKey is inactive");
        }

        log.setApiKey(apiKey);
        log.setEndpoint(log.getEndpoint().trim());
        if (log.getTimestamp() == null) log.setTimestamp(Instant.now());

        return apiUsageLogRepository.save(log);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApiUsageLog> getUsageForApiKey(Long keyId) {
        return apiUsageLogRepository.findByApiKeyId(keyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApiUsageLog> getUsageForToday(Long keyId) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zoneId);

        Instant start = today.atStartOfDay(zoneId).toInstant();
        Instant end = today.plusDays(1).atStartOfDay(zoneId).toInstant();

        return apiUsageLogRepository.findForKeyBetween(keyId, start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public int countRequestsToday(Long keyId) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zoneId);

        Instant start = today.atStartOfDay(zoneId).toInstant();
        Instant end = today.plusDays(1).atStartOfDay(zoneId).toInstant();

        return apiUsageLogRepository.countForKeyBetween(keyId, start, end);
    }
}