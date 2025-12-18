package com.example.demo.controller;

import com.example.demo.entity.ApiKey;
import com.example.demo.service.ApiKeyService;
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
    public ApiKey create(@RequestBody ApiKey key) {
        return apiKeyService.createApiKey(key);
    }

    @PutMapping("/{id}")
    public ApiKey update(@PathVariable Long id, @RequestBody ApiKey key) {
        return apiKeyService.updateApiKey(id, key);
    }

    @GetMapping("/{id}")
    public ApiKey getById(@PathVariable Long id) {
        return apiKeyService.getApiKeyById(id);
    }

    @GetMapping
    public List<ApiKey> getAll() {
        return apiKeyService.getAllApiKeys();
    }

    @PutMapping("/{id}/deactivate")
    public ApiKey deactivate(@PathVariable Long id) {
        return apiKeyService.deactivateApiKey(id);
    }
}