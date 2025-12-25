package com.example.demo.controller;

import com.example.demo.entity.KeyExemption;
import com.example.demo.service.KeyExemptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exemptions")
public class KeyExemptionController {

    private final KeyExemptionService exemptionService;

    public KeyExemptionController(KeyExemptionService exemptionService) {
        this.exemptionService = exemptionService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<KeyExemption> createExemption(@Valid @RequestBody KeyExemption dto) {
        return ResponseEntity.ok(exemptionService.createExemption(dto));
    }

    // UPDATE by apiKeyId
    @PutMapping("/key/{apiKeyId}")
    public ResponseEntity<KeyExemption> updateExemption(
            @PathVariable Long apiKeyId,
            @Valid @RequestBody KeyExemption dto
    ) {
        return ResponseEntity.ok(exemptionService.updateExemption(apiKeyId, dto));
    }

    // GET by apiKeyId
    @GetMapping("/key/{apiKeyId}")
    public ResponseEntity<KeyExemption> getExemptionByKey(@PathVariable Long apiKeyId) {
        return ResponseEntity.ok(exemptionService.getExemptionByKey(apiKeyId));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<KeyExemption>> getAllExemptions() {
        return ResponseEntity.ok(exemptionService.getAllExemptions());
    }
}