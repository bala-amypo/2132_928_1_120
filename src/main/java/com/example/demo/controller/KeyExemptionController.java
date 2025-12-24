package com.example.demo.controller;

import com.example.demo.dto.KeyExemptionDto;
import com.example.demo.service.KeyExemptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    public ResponseEntity<KeyExemptionDto> create(@Valid @RequestBody KeyExemptionDto dto) {
        KeyExemptionDto saved = exemptionService.createExemption(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KeyExemptionDto> update(@PathVariable Long id, @Valid @RequestBody KeyExemptionDto dto) {
        KeyExemptionDto updated = exemptionService.updateExemption(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/key/{keyId}")
    public ResponseEntity<KeyExemptionDto> getByKey(@PathVariable Long keyId) {
        return ResponseEntity.ok(exemptionService.getExemptionByKey(keyId));
    }

    @GetMapping
    public ResponseEntity<List<KeyExemptionDto>> getAll() {
        return ResponseEntity.ok(exemptionService.getAllExemptions());
    }
}