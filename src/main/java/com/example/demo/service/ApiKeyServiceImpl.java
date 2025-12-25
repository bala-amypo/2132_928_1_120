package com.example.demo.service;

import com.example.demo.entity.ApiKey;
import com.example.demo.entity.QuotaPlan;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.QuotaPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public ApiKey createApiKey(ApiKey key) {
        if (key == null) throw new BadRequestException("ApiKey is required");

        if (key.getKeyValue() == null || key.getKeyValue().trim().isEmpty()) {
            throw new BadRequestException("Key value is required");
        }
        if (key.getOwnerId() == null) {
            throw new BadRequestException("OwnerId is required");
        }
        if (key.getPlan() == null || key.getPlan().getId() == null) {
            throw new BadRequestException("Plan is required");
        }

        apiKeyRepository.findByKeyValue(key.getKeyValue().trim())
                .ifPresent(x -> { throw new BadRequestException("API Key already exists"); });

        QuotaPlan plan = quotaPlanRepository.findById(key.getPlan().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Quota plan not found"));

        if (!plan.isActive()) {
            throw new BadRequestException("Quota plan is inactive");
        }

        key.setKeyValue(key.getKeyValue().trim());
        key.setPlan(plan);
        if (key.getActive() == null) key.setActive(true);

        return apiKeyRepository.save(key);
    }

    @Override
    public ApiKey updateApiKey(Long id, ApiKey input) {
        ApiKey key = apiKeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("API Key not found"));

        if (!key.isActive()) {
            throw new BadRequestException("Cannot update inactive key");
        }
        if (input.getOwnerId() == null) {
            throw new BadRequestException("OwnerId is required");
        }

        key.setOwnerId(input.getOwnerId());
        return apiKeyRepository.save(key);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiKey getApiKeyById(Long id) {
        return apiKeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("API Key not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiKey getApiKeyByValue(String keyValue) {
        return apiKeyRepository.findByKeyValue(keyValue)
                .orElseThrow(() -> new ResourceNotFoundException("API Key not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApiKey> getAllApiKeys() {
        return apiKeyRepository.findAll();
    }

    @Override
    public void deactivateApiKey(Long id) {
        ApiKey key = apiKeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("API Key not found"));
        key.setActive(false);
        apiKeyRepository.save(key);
    }
}