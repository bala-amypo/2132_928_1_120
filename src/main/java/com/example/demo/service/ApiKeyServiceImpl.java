package com.example.demo.service;

import com.example.demo.entity.ApiKey;
import com.example.demo.entity.QuotaPlan;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.QuotaPlanRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final QuotaPlanRepository quotaPlanRepository;

    public ApiKeyServiceImpl(ApiKeyRepository apiKeyRepository, QuotaPlanRepository quotaPlanRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.quotaPlanRepository = quotaPlanRepository;
    }

    @Override
    public ApiKey createApiKey(ApiKey key) {
        if (key.getPlan() == null || key.getPlan().getId() == null) {
            throw new RuntimeException("BadRequest: plan is required");
        }
        QuotaPlan plan = quotaPlanRepository.findById(key.getPlan().getId())
                .orElseThrow(() -> new RuntimeException("ResourceNotFound: plan not found"));

        if (Boolean.FALSE.equals(plan.getActive())) {
            throw new RuntimeException("BadRequest: plan is inactive");
        }
        key.setPlan(plan);
        key.setCreatedAt(Instant.now());
        key.setUpdatedAt(Instant.now());
        if (key.getActive() == null) key.setActive(true);
        return apiKeyRepository.save(key);
    }

    @Override
    public ApiKey updateApiKey(Long id, ApiKey key) {
        ApiKey existing = getApiKeyById(id);

        if (key.getKeyValue() != null) existing.setKeyValue(key.getKeyValue());
        if (key.getOwnerId() != null) existing.setOwnerId(key.getOwnerId());
        if (key.getActive() != null) existing.setActive(key.getActive());

        if (key.getPlan() != null && key.getPlan().getId() != null) {
            QuotaPlan plan = quotaPlanRepository.findById(key.getPlan().getId())
                    .orElseThrow(() -> new RuntimeException("ResourceNotFound: plan not found"));
            if (Boolean.FALSE.equals(plan.getActive())) {
                throw new RuntimeException("BadRequest: plan is inactive");
            }
            existing.setPlan(plan);
        }

        existing.setUpdatedAt(Instant.now());
        return apiKeyRepository.save(existing);
    }

    @Override
    public ApiKey getApiKeyById(Long id) {
        return apiKeyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ResourceNotFound: api key not found"));
    }

    @Override
    public ApiKey getApiKeyByValue(String keyValue) {
        return apiKeyRepository.findByKeyValue(keyValue)
                .orElseThrow(() -> new RuntimeException("ResourceNotFound: api key not found"));
    }

    @Override
    public List<ApiKey> getAllApiKeys() {
        return apiKeyRepository.findAll();
    }

    @Override
    public ApiKey deactivateApiKey(Long id) {
        ApiKey existing = getApiKeyById(id);
        existing.setActive(false);
        existing.setUpdatedAt(Instant.now());
        return apiKeyRepository.save(existing);
    }
}