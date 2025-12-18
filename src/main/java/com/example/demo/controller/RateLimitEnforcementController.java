package com.example.demo.controller;

import com.example.demo.entity.RateLimitEnforcement;
import com.example.demo.service.RateLimitEnforcementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enforcements")
public class RateLimitEnforcementController {

    private final RateLimitEnforcementService service;

    public RateLimitEnforcementController(RateLimitEnforcementService service) {
        this.service = service;
    }

    @PostMapping
    public RateLimitEnforcement create(@RequestBody RateLimitEnforcement enforcement) {
        return service.createEnforcement(enforcement);
    }

    @GetMapping("/{id}")
    public RateLimitEnforcement getById(@PathVariable Long id) {
        return service.getEnforcementById(id);
    }

    @GetMapping("/key/{keyId}")
    public List<RateLimitEnforcement> forKey(@PathVariable Long keyId) {
        return service.getEnforcementsForKey(keyId);
    }
}