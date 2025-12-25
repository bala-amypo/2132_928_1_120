package com.example.demo.controller;

import com.example.demo.entity.KeyExemption;
import com.example.demo.service.KeyExemptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exemptions")
public class KeyExemptionController {

    private final KeyExemptionService exemptionService;

    public KeyExemptionController(KeyExemptionService exemptionService) {
        this.exemptionService = exemptionService;
    }

    @PostMapping
    public ResponseEntity<KeyExemption> create(@Valid @RequestBody KeyExemption exemption) {
        return ResponseEntity.ok(exemptionService.createExemption(exemption));
    }

    @PutMapping("/{apiKeyId}")
    public ResponseEntity<KeyExemption> update(@PathVariable Long apiKeyId, @Valid @RequestBody KeyExemption exemption) {
        return ResponseEntity.ok(exemptionService.updateExemption(apiKeyId, exemption));
    }

    @GetMapping("/key/{apiKeyId}")
    public ResponseEntity<KeyExemption> getByKey(@PathVariable Long apiKeyId) {
        return ResponseEntity.ok(exemptionService.getExemptionByKey(apiKeyId));
    }

    @GetMapping
    public ResponseEntity<List<KeyExemption>> getAll() {
        return ResponseEntity.ok(exemptionService.getAllExemptions());
    }
}