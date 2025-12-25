package com.example.demo.controller;

import com.example.demo.dto.ApiKeyDto;
import com.example.demo.mapper.DtoMapper;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.entity.ApiKey;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keys")
public class ApiKeyController {

    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyController(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @PostMapping
    public ResponseEntity<ApiKeyDto> create(@Valid @RequestBody ApiKeyDto dto) {
        ApiKey saved = apiKeyRepository.save(DtoMapper.toEntity(dto));
        return ResponseEntity.ok(DtoMapper.toDto(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiKeyDto> get(@PathVariable Long id) {
        ApiKey e = apiKeyRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(DtoMapper.toDto(e));
    }

    @GetMapping
    public ResponseEntity<List<ApiKeyDto>> getAll() {
        return ResponseEntity.ok(DtoMapper.toApiKeyDtos(apiKeyRepository.findAll()));
    }
}