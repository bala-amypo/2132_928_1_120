package com.example.demo.controller;

import com.example.demo.dto.RateLimitEnforcementRequestDto;
import com.example.demo.entity.RateLimitEnforcement;
import com.example.demo.service.RateLimitEnforcementService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/enforcements")
public class RateLimitEnforcementController {

    private final RateLimitEnforcementService service;

    public RateLimitEnforcementController(RateLimitEnforcementService service) {
        this.service = service;
    }

    // POST /api/enforcements
    @PostMapping
    public RateLimitEnforcement create(@Valid @RequestBody RateLimitEnforcementRequestDto dto) {
        return service.create(dto);
    }

    // GET /api/enforcements/{id}
    @GetMapping("/{id}")
    public RateLimitEnforcement getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // GET /api/enforcements/key/{keyId}
    @GetMapping("/key/{keyId}")
    public List<RateLimitEnforcement> getByKey(@PathVariable Long keyId) {
        return service.getByApiKey(keyId);
    }

    // GET /api/enforcements/key/{keyId}/between?from=...&to=...
    @GetMapping("/key/{keyId}/between")
    public List<RateLimitEnforcement> getByKeyBetween(
            @PathVariable Long keyId,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to
    ) {
        return service.getByApiKeyBetween(keyId, from, to);
    }
}