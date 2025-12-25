package com.example.demo.service;

import com.example.demo.dto.ApiUsageLogDto;
import com.example.demo.entity.ApiKey;
import com.example.demo.entity.ApiUsageLog;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.ApiUsageLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
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
        ApiKey apiKey = apiKeyRepository.findById(dto.getApiKeyId())
                .orElseThrow(() -> new ResourceNotFoundException("API key not found: " + dto.getApiKeyId()));

        ApiUsageLog log = new ApiUsageLog();
        log.setApiKey(apiKey);
        log.setRequestTimestamp(dto.getRequestTimestamp() != null ? dto.getRequestTimestamp() : Instant.now());
        log.setEndpoint(dto.getEndpoint());
        log.setHttpMethod(dto.getHttpMethod());
        log.setSuccess(dto.isSuccess());
        log.setResponseCode(dto.getResponseCode());

        ApiUsageLog saved = apiUsageLogRepository.save(log);
        dto.setId(saved.getId());
        return dto;
    }

    @Override
    public List<ApiUsageLogDto> getByApiKey(Long apiKeyId) {
        return apiUsageLogRepository.findByApiKey_Id(apiKeyId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ApiUsageLogDto> getForKeyBetween(Long apiKeyId, Instant from, Instant to) {
        return apiUsageLogRepository.findForKeyBetween(apiKeyId, from, to)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public long countForKeyBetween(Long apiKeyId, Instant from, Instant to) {
        return apiUsageLogRepository.countForKeyBetween(apiKeyId, from, to);
    }

    @Override
    public long countRequestsToday(Long apiKeyId) {
        // ✅ start/end of UTC day (simple & consistent for tests)
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        Instant from = today.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant to = today.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        return apiUsageLogRepository.countForKeyBetween(apiKeyId, from, to);
    }

    private ApiUsageLogDto toDto(ApiUsageLog e) {
        ApiUsageLogDto dto = new ApiUsageLogDto();
        dto.setId(e.getId());
        dto.setApiKeyId(e.getApiKey() != null ? e.getApiKey().getId() : null);
        dto.setRequestTimestamp(e.getRequestTimestamp());
        dto.setEndpoint(e.getEndpoint());
        dto.setHttpMethod(e.getHttpMethod());
        dto.setSuccess(e.isSuccess());
        dto.setResponseCode(e.getResponseCode());
        return dto;
    }
}