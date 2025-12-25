package com.example.demo.controller;

import com.example.demo.entity.RateLimitEnforcement;
import com.example.demo.service.RateLimitEnforcementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enforcements")
public class RateLimitEnforcementController {

    private final RateLimitEnforcementService enforcementService;

    public RateLimitEnforcementController(RateLimitEnforcementService enforcementService) {
        this.enforcementService = enforcementService;
    }

    @PostMapping
    public ResponseEntity<RateLimitEnforcement> create(@Valid @RequestBody RateLimitEnforcement enforcement) {
        return ResponseEntity.ok(enforcementService.createEnforcement(enforcement));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RateLimitEnforcement> getById(@PathVariable Long id) {
        return ResponseEntity.ok(enforcementService.getEnforcementById(id));
    }

    @GetMapping("/key/{apiKeyId}")
    public ResponseEntity<List<RateLimitEnforcement>> getForKey(@PathVariable Long apiKeyId) {
        return ResponseEntity.ok(enforcementService.getEnforcementsForKey(apiKeyId));
    }
}