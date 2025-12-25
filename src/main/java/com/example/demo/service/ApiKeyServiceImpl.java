package com.example.demo.service;

import com.example.demo.dto.ApiKeyDto;
import com.example.demo.entity.ApiKey;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ApiKeyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyServiceImpl(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public ApiKeyDto createApiKey(ApiKeyDto dto) {
        ApiKey e = new ApiKey();
        e.setApiKey(dto.getApiKey());
        e.setOwnerEmail(dto.getOwnerEmail());
        ApiKey saved = apiKeyRepository.save(e);
        return toDto(saved);
    }

    @Override
    public ApiKeyDto getApiKey(Long id) {
        ApiKey e = apiKeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found: " + id));
        return toDto(e);
    }

    @Override
    public List<ApiKeyDto> getAllApiKeys() {
        return apiKeyRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteApiKey(Long id) {
        if (!apiKeyRepository.existsById(id)) {
            throw new ResourceNotFoundException("ApiKey not found: " + id);
        }
        apiKeyRepository.deleteById(id);
    }

    private ApiKeyDto toDto(ApiKey e) {
        ApiKeyDto dto = new ApiKeyDto();
        dto.setId(e.getId());
        dto.setApiKey(e.getApiKey());
        dto.setOwnerEmail(e.getOwnerEmail());
        return dto;
    }
}