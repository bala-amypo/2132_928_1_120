package com.example.demo.service;

import com.example.demo.dto.ApiKeyDto;
import com.example.demo.entity.ApiKey;
import com.example.demo.entity.QuotaPlan;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.QuotaPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final QuotaPlanRepository quotaPlanRepository;

    public ApiKeyServiceImpl(ApiKeyRepository apiKeyRepository,
                             QuotaPlanRepository quotaPlanRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.quotaPlanRepository = quotaPlanRepository;
    }

    @Override
    public ApiKeyDto createApiKey(ApiKeyDto dto) {

        if (dto.getKeyValue() == null || dto.getKeyValue().trim().isEmpty()) {
            throw new BadRequestException("Key value is required");
        }
        if (dto.getOwnerId() == null) {
            throw new BadRequestException("OwnerId is required");
        }
        if (dto.getPlanId() == null) {
            throw new BadRequestException("PlanId is required");
        }

        apiKeyRepository.findByKeyValue(dto.getKeyValue().trim())
                .ifPresent(x -> { throw new BadRequestException("API Key already exists"); });

        QuotaPlan plan = quotaPlanRepository.findById(dto.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Quota plan not found"));

        if (!plan.isActive()) {
            throw new BadRequestException("Quota plan is inactive");
        }

        ApiKey key = new ApiKey();
        key.setKeyValue(dto.getKeyValue().trim());
        key.setOwnerId(dto.getOwnerId());
        key.setPlan(plan);
        key.setActive(true);

        ApiKey saved = apiKeyRepository.save(key);
        return toDto(saved);
    }

    @Override
    public ApiKeyDto updateApiKey(Long id, ApiKeyDto dto) {

        ApiKey key = apiKeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("API Key not found"));

        if (!key.isActive()) {
            throw new BadRequestException("Cannot update inactive key");
        }
        if (dto.getOwnerId() == null) {
            throw new BadRequestException("OwnerId is required");
        }

        key.setOwnerId(dto.getOwnerId());
        ApiKey saved = apiKeyRepository.save(key);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiKeyDto getApiKeyById(Long id) {
        ApiKey key = apiKeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("API Key not found"));
        return toDto(key);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiKeyDto getApiKeyByValue(String keyValue) {
        ApiKey key = apiKeyRepository.findByKeyValue(keyValue)
                .orElseThrow(() -> new ResourceNotFoundException("API Key not found"));
        return toDto(key);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApiKeyDto> getAllApiKeys() {
        return apiKeyRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deactivateApiKey(Long id) {
        ApiKey key = apiKeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("API Key not found"));
        key.setActive(false);
        apiKeyRepository.save(key);
    }

    private ApiKeyDto toDto(ApiKey key) {
        ApiKeyDto dto = new ApiKeyDto();
        dto.setId(key.getId());
        dto.setKeyValue(key.getKeyValue());
        dto.setOwnerId(key.getOwnerId());
        dto.setPlanId(key.getPlan().getId());
        dto.setActive(key.getActive());
        dto.setCreatedAt(key.getCreatedAt());
        dto.setUpdatedAt(key.getUpdatedAt());
        return dto;
    }
}