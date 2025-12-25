package com.example.demo.controller;

import com.example.demo.entity.ApiUsageLog;
import com.example.demo.service.ApiUsageLogService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usage")
public class ApiUsageLogController {

    private final ApiUsageLogService usageService;

    public ApiUsageLogController(ApiUsageLogService usageService) {
        this.usageService = usageService;
    }

    @PostMapping
    public ResponseEntity<ApiUsageLog> log(@Valid @RequestBody ApiUsageLog log) {
        return ResponseEntity.ok(usageService.logUsage(log));
    }

    @GetMapping("/key/{apiKeyId}")
    public ResponseEntity<List<ApiUsageLog>> getForKey(@PathVariable Long apiKeyId) {
        return ResponseEntity.ok(usageService.getUsageForApiKey(apiKeyId));
    }

    @GetMapping("/today/{apiKeyId}")
    public ResponseEntity<List<ApiUsageLog>> getToday(@PathVariable Long apiKeyId) {
        return ResponseEntity.ok(usageService.getUsageForToday(apiKeyId));
    }

    @GetMapping("/today/{apiKeyId}/count")
    public ResponseEntity<Integer> countToday(@PathVariable Long apiKeyId) {
        return ResponseEntity.ok(usageService.countRequestsToday(apiKeyId));
    }
}