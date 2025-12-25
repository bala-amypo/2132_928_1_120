package com.example.demo.controller;

import com.example.demo.dto.ApiKeyDto;
import com.example.demo.service.ApiKeyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keys")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @PostMapping
    public ResponseEntity<ApiKeyDto> create(@Valid @RequestBody ApiKeyDto dto) {
        return ResponseEntity.ok(apiKeyService.createApiKey(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiKeyDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(apiKeyService.getApiKey(id));
    }

    @GetMapping
    public ResponseEntity<List<ApiKeyDto>> getAll() {
        return ResponseEntity.ok(apiKeyService.getAllApiKeys());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        apiKeyService.deleteApiKey(id);
        return ResponseEntity.noContent().build();
    }
}