package com.example.demo.controller;

import com.example.demo.entity.ApiUsageLog;
import com.example.demo.service.ApiUsageLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usage-logs")
public class ApiUsageLogController {

    private final ApiUsageLogService apiUsageLogService;

    public ApiUsageLogController(ApiUsageLogService apiUsageLogService) {
        this.apiUsageLogService = apiUsageLogService;
    }

    @PostMapping
    public ApiUsageLog logUsage(@RequestBody ApiUsageLog log) {
        return apiUsageLogService.logUsage(log);
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