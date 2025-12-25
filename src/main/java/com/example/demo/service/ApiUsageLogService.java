package com.example.demo.service;

import com.example.demo.dto.ApiUsageLogDto;

import java.time.Instant;
import java.util.List;

public interface ApiUsageLogService {
    ApiUsageLogDto logUsage(ApiUsageLogDto dto);

    List<ApiUsageLogDto> getByApiKey(Long apiKeyId);

    List<ApiUsageLogDto> getForKeyBetween(Long apiKeyId, Instant from, Instant to);

    long countForKeyBetween(Long apiKeyId, Instant from, Instant to);

    // ✅ your compiler says this exists, so implement it
    long countRequestsToday(Long apiKeyId);
}