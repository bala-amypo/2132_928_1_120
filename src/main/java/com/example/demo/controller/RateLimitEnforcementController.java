package com.example.demo.controller;

import com.example.demo.dto.RateLimitEnforcementDto;
import com.example.demo.service.RateLimitEnforcementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    public ResponseEntity<RateLimitEnforcementDto> create(@Valid @RequestBody RateLimitEnforcementDto dto) {
        RateLimitEnforcementDto saved = enforcementService.createEnforcement(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RateLimitEnforcementDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(enforcementService.getEnforcementById(id));
    }

    @GetMapping("/key/{keyId}")
    public ResponseEntity<List<RateLimitEnforcementDto>> getForKey(@PathVariable Long keyId) {
        return ResponseEntity.ok(enforcementService.getEnforcementsForKey(keyId));
    }
}