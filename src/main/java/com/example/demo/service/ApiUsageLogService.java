package com.example.demo.service;

import com.example.demo.entity.ApiUsageLog;

import java.util.List;

public interface ApiUsageLogService {

    ApiUsageLog logUsage(Long apiKeyId, String endpoint);

    List<ApiUsageLog> getUsageForApiKey(Long keyId);

    List<ApiUsageLog> getUsageForToday(Long keyId);

    long countRequestsToday(Long keyId);
}