package com.example.demo.controller;

import com.example.demo.entity.ApiKey;
import com.example.demo.service.ApiKeyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-keys")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @PostMapping
    public ResponseEntity<ApiKey> create(@Valid @RequestBody ApiKey apiKey) {
        return ResponseEntity.ok(apiKeyService.createApiKey(apiKey));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiKey> getById(@PathVariable Long id) {
        return ResponseEntity.ok(apiKeyService.getApiKeyById(id));
    }

    @GetMapping("/by-value")
    public ResponseEntity<ApiKey> getByValue(@RequestParam("value") String value) {
        return ResponseEntity.ok(apiKeyService.getApiKeyByValue(value));
    }

    @GetMapping
    public ResponseEntity<List<ApiKey>> getAll() {
        return ResponseEntity.ok(apiKeyService.getAllApiKeys());
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivate(@PathVariable Long id) {
        apiKeyService.deactivateApiKey(id);
        return ResponseEntity.ok("API key deactivated");
    }
}