package com.example.demo.controller;

import com.example.demo.dto.ApiUsageLogRequestDto;
import com.example.demo.entity.ApiUsageLog;
import com.example.demo.service.ApiUsageLogService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/usage-logs")
public class ApiUsageLogController {

    private final ApiUsageLogService apiUsageLogService;

    public ApiUsageLogController(ApiUsageLogService apiUsageLogService) {
        this.apiUsageLogService = apiUsageLogService;
    }

    
    @PostMapping
    public ApiUsageLog logUsage(@Valid @RequestBody ApiUsageLogRequestDto req) {
        return apiUsageLogService.logUsage(req.getApiKeyId(), req.getEndpoint());
    }

    @GetMapping("/key/{keyId}")
    public List<ApiUsageLog> getForKey(@PathVariable Long keyId) {
        return apiUsageLogService.getUsageForApiKey(keyId);
    }

    @GetMapping("/key/{keyId}/today")
    public List<ApiUsageLog> getToday(@PathVariable Long keyId) {
        return apiUsageLogService.getUsageForToday(keyId);
    }

    @GetMapping("/key/{keyId}/count-today")
    public long countToday(@PathVariable Long keyId) {
        return apiUsageLogService.countRequestsToday(keyId);
    }
}