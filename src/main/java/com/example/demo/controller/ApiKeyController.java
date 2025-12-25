package com.example.demo.controller;

import com.example.demo.dto.ApiKeyDto;
import com.example.demo.service.ApiKeyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiKeyDto> createApiKey(@Valid @RequestBody ApiKeyDto dto) {
        ApiKeyDto created = apiKeyService.createApiKey(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiKeyDto> updateApiKey(@PathVariable Long id, @Valid @RequestBody ApiKeyDto dto) {
        ApiKeyDto updated = apiKeyService.updateApiKey(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiKeyDto> getApiKeyById(@PathVariable Long id) {
        return ResponseEntity.ok(apiKeyService.getApiKeyById(id));
    }

    @GetMapping
    public ResponseEntity<List<ApiKeyDto>> getAllApiKeys() {
        return ResponseEntity.ok(apiKeyService.getAllApiKeys());
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        apiKeyService.deactivateApiKey(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-value/{keyValue}")
    public ResponseEntity<ApiKeyDto> getByKeyValue(@PathVariable String keyValue) {
        return ResponseEntity.ok(apiKeyService.getApiKeyByValue(keyValue));
    }
}