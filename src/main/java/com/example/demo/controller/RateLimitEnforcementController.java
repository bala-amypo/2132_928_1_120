package com.example.demo.controller;

import com.example.demo.entity.RateLimitEnforcement;
import com.example.demo.service.RateLimitEnforcementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/enforcements")
public class RateLimitEnforcementController {

    private final RateLimitEnforcementService service;

    public RateLimitEnforcementController(RateLimitEnforcementService service) {
        this.service = service;
    }

    @PostMapping
    public RateLimitEnforcement create(@Valid @RequestBody RateLimitEnforcement enforcement) {
        return service.createEnforcement(enforcement);
    }

    @GetMapping("/{id}")
    public RateLimitEnforcement getById(@PathVariable @Min(1) Long id) {
        return service.getEnforcementById(id);
    }

    @GetMapping("/key/{keyId}")
    public List<RateLimitEnforcement> forKey(@PathVariable @Min(1) Long keyId) {
        return service.getEnforcementsForKey(keyId);
    }
}