package com.example.demo.controller;

import com.example.demo.dto.ApiKeyRequestDto;
import com.example.demo.dto.ApiKeyResponseDto;
import com.example.demo.service.ApiKeyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public ApiKeyResponseDto create(@Valid @RequestBody ApiKeyRequestDto dto) {
        return apiKeyService.create(dto);
    }

    @GetMapping("/{id}")
    public ApiKeyResponseDto getById(@PathVariable Long id) {
        return apiKeyService.getById(id);
    }

    // ✅ NEW: HQL GET BY keyValue
    // Swagger: GET /api/api-keys/by-key/{keyValue}
    @GetMapping("/by-key/{keyValue}")
    public ApiKeyResponseDto getByKeyValue(@PathVariable String keyValue) {
        return apiKeyService.getByKeyValue(keyValue);
    }

    @GetMapping
    public List<ApiKeyResponseDto> getAll() {
        return apiKeyService.getAll();
    }

    // ✅ NEW: HQL GET ONLY ACTIVE
    // Swagger: GET /api/api-keys/active
    @GetMapping("/active")
    public List<ApiKeyResponseDto> getAllActive() {
        return apiKeyService.getAllActive();
    }

    @PutMapping("/{id}")
    public ApiKeyResponseDto update(@PathVariable Long id, @Valid @RequestBody ApiKeyRequestDto dto) {
        return apiKeyService.update(id, dto);
    }

    @PutMapping("/{id}/deactivate")
    public ApiKeyResponseDto deactivate(@PathVariable Long id) {
        return apiKeyService.deactivate(id);
    }
}