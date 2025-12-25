package com.example.demo.service;

import com.example.demo.dto.ApiUsageLogDto;
import java.util.List;

public interface ApiUsageLogService {
    ApiUsageLogDto logUsage(ApiUsageLogDto dto);
    List<ApiUsageLogDto> getUsageForApiKey(Long keyId);
    List<ApiUsageLogDto> getUsageForToday(Long keyId);
    int countRequestsToday(Long keyId);
}