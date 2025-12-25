package com.example.demo.controller;

import com.example.demo.dto.KeyExemptionDto;
import com.example.demo.service.KeyExemptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/key-exemptions")
public class KeyExemptionController {

    private final KeyExemptionService exemptionService;

    public KeyExemptionController(KeyExemptionService exemptionService) {
        this.exemptionService = exemptionService;
    }

    // ✅ replaces createExemption(...)
    @PostMapping
    public ResponseEntity<KeyExemptionDto> create(@Valid @RequestBody KeyExemptionDto dto) {
        return ResponseEntity.ok(exemptionService.upsertExemption(dto));
    }

    // ✅ replaces updateExemption(id, dto)
    // We ignore path id and rely on dto.apiKeyId for upsert (or you can enforce match)
    @PutMapping("/{id}")
    public ResponseEntity<KeyExemptionDto> update(@PathVariable Long id, @Valid @RequestBody KeyExemptionDto dto) {
        // optional safety: keep id as dto.id
        dto.setId(id);
        return ResponseEntity.ok(exemptionService.upsertExemption(dto));
    }

    // ✅ replaces getExemptionByKey(apiKeyId)
    @GetMapping("/by-key/{apiKeyId}")
    public ResponseEntity<List<KeyExemptionDto>> getByKey(@PathVariable Long apiKeyId) {
        return ResponseEntity.ok(exemptionService.getExemptionsForKey(apiKeyId));
    }

    @GetMapping
    public ResponseEntity<List<KeyExemptionDto>> getAll() {
        return ResponseEntity.ok(exemptionService.getAllExemptions());
    }
}