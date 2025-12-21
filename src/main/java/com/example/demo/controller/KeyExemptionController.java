// src/main/java/com/example/demo/controller/KeyExemptionController.java
package com.example.demo.controller;

import com.example.demo.dto.KeyExemptionRequestDto;
import com.example.demo.dto.KeyExemptionResponseDto;
import com.example.demo.service.KeyExemptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/key-exemptions")
public class KeyExemptionController {

    private final KeyExemptionService keyExemptionService;

    public KeyExemptionController(KeyExemptionService keyExemptionService) {
        this.keyExemptionService = keyExemptionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public KeyExemptionResponseDto create(@Valid @RequestBody KeyExemptionRequestDto dto) {
        return keyExemptionService.create(dto);
    }

    @PutMapping("/{id}")
    public KeyExemptionResponseDto update(@PathVariable Long id, @Valid @RequestBody KeyExemptionRequestDto dto) {
        return keyExemptionService.update(id, dto);
    }

    @GetMapping
    public List<KeyExemptionResponseDto> getAll() {
        return keyExemptionService.getAllExemptions();
    }

    @GetMapping("/{id}")
    public KeyExemptionResponseDto getById(@PathVariable Long id) {
        return keyExemptionService.getById(id);
    }

    @GetMapping("/by-api-key/{apiKeyId}")
    public List<KeyExemptionResponseDto> getByApiKey(@PathVariable Long apiKeyId) {
        return keyExemptionService.getByApiKeyId(apiKeyId);
    }
}