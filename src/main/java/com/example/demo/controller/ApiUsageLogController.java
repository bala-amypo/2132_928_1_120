package com.example.demo.controller;

import com.example.demo.entity.ApiUsageLog;
import com.example.demo.service.ApiUsageLogService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
    public ApiUsageLog logUsage(@Valid @RequestBody ApiUsageLog log) {
        return apiUsageLogService.logUsage(log);
    }

    @GetMapping("/key/{keyId}")
    public List<ApiUsageLog> getForKey(@PathVariable @Min(1) Long keyId) {
        return apiUsageLogService.getUsageForApiKey(keyId);
    }

    @GetMapping("/key/{keyId}/today")
    public List<ApiUsageLog> getToday(@PathVariable @Min(1) Long keyId) {
        return apiUsageLogService.getUsageForToday(keyId);
    }

    @GetMapping("/key/{keyId}/count-today")
    public long countToday(@PathVariable @Min(1) Long keyId) {
        return apiUsageLogService.countRequestsToday(keyId);
    }
}