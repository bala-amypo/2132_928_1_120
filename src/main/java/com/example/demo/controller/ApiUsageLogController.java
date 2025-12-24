package com.example.demo.controller;

import com.example.demo.dto.ApiUsageLogDto;
import com.example.demo.dto.CountResponseDto;
import com.example.demo.service.ApiUsageLogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiUsageLogDto> logUsage(@Valid @RequestBody ApiUsageLogDto dto) {
        ApiUsageLogDto saved = apiUsageLogService.logUsage(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/key/{keyId}")
    public ResponseEntity<List<ApiUsageLogDto>> getForKey(@PathVariable Long keyId) {
        return ResponseEntity.ok(apiUsageLogService.getUsageForApiKey(keyId));
    }

    @GetMapping("/key/{keyId}/today")
    public ResponseEntity<List<ApiUsageLogDto>> getToday(@PathVariable Long keyId) {
        return ResponseEntity.ok(apiUsageLogService.getUsageForToday(keyId));
    }

    @GetMapping("/key/{keyId}/count-today")
    public ResponseEntity<CountResponseDto> countToday(@PathVariable Long keyId) {
        long count = apiUsageLogService.countRequestsToday(keyId);
        return ResponseEntity.ok(new CountResponseDto(count));
    }
}