package com.example.demo.controller;

import com.example.demo.dto.RateLimitEnforcementDto;
import com.example.demo.service.RateLimitEnforcementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enforcements")
public class RateLimitEnforcementController {

    private final RateLimitEnforcementService enforcementService;

    public RateLimitEnforcementController(RateLimitEnforcementService enforcementService) {
        this.enforcementService = enforcementService;
    }

    // ✅ replaces createEnforcement(...)
    @PostMapping
    public ResponseEntity<RateLimitEnforcementDto> create(@Valid @RequestBody RateLimitEnforcementDto dto) {
        return ResponseEntity.ok(enforcementService.upsertEnforcement(dto));
    }

    // ✅ replaces getEnforcementById(id) (your service is key-based now)
    // This endpoint fetches by apiKeyId
    @GetMapping("/by-key/{apiKeyId}")
    public ResponseEntity<List<RateLimitEnforcementDto>> getByKey(@PathVariable Long apiKeyId) {
        return ResponseEntity.ok(enforcementService.getEnforcementsForKey(apiKeyId));
    }

    @GetMapping
    public ResponseEntity<List<RateLimitEnforcementDto>> getAll() {
        return ResponseEntity.ok(enforcementService.getAllEnforcements());
    }
}