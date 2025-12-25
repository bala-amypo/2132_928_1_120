package com.example.demo.controller;

import com.example.demo.entity.ApiKey;
import com.example.demo.service.ApiKeyService;
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
    public ResponseEntity<ApiKey> create(@RequestBody ApiKey key) {
        return ResponseEntity.status(HttpStatus.CREATED).body(apiKeyService.createApiKey(key));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiKey> update(@PathVariable Long id, @RequestBody ApiKey key) {
        return ResponseEntity.ok(apiKeyService.updateApiKey(id, key));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiKey> getById(@PathVariable Long id) {
        return ResponseEntity.ok(apiKeyService.getApiKeyById(id));
    }

    @GetMapping
    public ResponseEntity<List<ApiKey>> getAll() {
        return ResponseEntity.ok(apiKeyService.getAllApiKeys());
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        apiKeyService.deactivateApiKey(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-value/{keyValue}")
    public ResponseEntity<ApiKey> getByValue(@PathVariable String keyValue) {
        return ResponseEntity.ok(apiKeyService.getApiKeyByValue(keyValue));
    }
}