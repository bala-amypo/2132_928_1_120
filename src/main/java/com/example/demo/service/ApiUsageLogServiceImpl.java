package com.example.demo.service;

import com.example.demo.dto.ApiUsageLogDto;
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
import java.util.stream.Collectors;

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
    public ApiUsageLogDto logUsage(ApiUsageLogDto dto) {

        if (dto.getTimestamp() != null && dto.getTimestamp().isAfter(Instant.now())) {
            throw new BadRequestException("timestamp cannot be in the future");
        }

        ApiKey apiKey = apiKeyRepository.findById(dto.getApiKeyId())
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found"));

        if (!Boolean.TRUE.equals(apiKey.getActive())) {
            throw new BadRequestException("ApiKey is inactive");
        }

        ApiUsageLog log = new ApiUsageLog();
        log.setApiKey(apiKey);
        log.setEndpoint(dto.getEndpoint());
        log.setTimestamp(dto.getTimestamp() != null ? dto.getTimestamp() : Instant.now());

        ApiUsageLog saved = apiUsageLogRepository.save(log);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApiUsageLogDto> getUsageForApiKey(Long keyId) {
        return apiUsageLogRepository.findByApiKeyId(keyId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApiUsageLogDto> getUsageForToday(Long keyId) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zoneId);

        Instant start = today.atStartOfDay(zoneId).toInstant();
        Instant end = today.plusDays(1).atStartOfDay(zoneId).toInstant();

        return apiUsageLogRepository.findForKeyBetween(keyId, start, end)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
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

    private ApiUsageLogDto toDto(ApiUsageLog log) {
        ApiUsageLogDto dto = new ApiUsageLogDto();
        dto.setId(log.getId());
        dto.setApiKeyId(log.getApiKey().getId());
        dto.setEndpoint(log.getEndpoint());
        dto.setTimestamp(log.getTimestamp());
        return dto;
    }
}