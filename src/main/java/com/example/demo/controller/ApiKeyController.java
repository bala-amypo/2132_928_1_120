package com.example.demo.controller;

import com.example.demo.dto.ApiKeyRequestDto;
import com.example.demo.dto.ApiKeyUpdateDto;
import com.example.demo.entity.ApiKey;
import com.example.demo.service.ApiKeyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/api-keys")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @PostMapping
    public ResponseEntity<ApiKey> create(@Valid @RequestBody ApiKeyRequestDto dto) {
        return ResponseEntity.ok(apiKeyService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<ApiKey>> getAll() {
        return ResponseEntity.ok(apiKeyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiKey> getById(@PathVariable Long id) {
        return ResponseEntity.ok(apiKeyService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiKey> update(@PathVariable Long id,
                                         @Valid @RequestBody ApiKeyUpdateDto dto) {
        return ResponseEntity.ok(apiKeyService.update(id, dto));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ApiKey> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(apiKeyService.deactivate(id));
    }
}