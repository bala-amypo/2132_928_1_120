package com.example.demo.controller;

import com.example.demo.entity.ApiKey;
import com.example.demo.service.ApiKeyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/api-keys")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @PostMapping
    public ApiKey create(@Valid @RequestBody ApiKey key) {
        return apiKeyService.createApiKey(key);
    }

    @PutMapping("/{id}")
    public ApiKey update(@PathVariable @Min(1) Long id, @Valid @RequestBody ApiKey key) {
        return apiKeyService.updateApiKey(id, key);
    }

    @GetMapping("/{id}")
    public ApiKey getById(@PathVariable @Min(1) Long id) {
        return apiKeyService.getApiKeyById(id);
    }

    @GetMapping
    public List<ApiKey> getAll() {
        return apiKeyService.getAllApiKeys();
    }

    @PutMapping("/{id}/deactivate")
    public ApiKey deactivate(@PathVariable @Min(1) Long id) {
        return apiKeyService.deactivateApiKey(id);
    }
}