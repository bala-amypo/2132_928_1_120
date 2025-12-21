package com.example.demo.service;

import com.example.demo.dto.ApiKeyRequestDto;
import com.example.demo.dto.ApiKeyUpdateDto;
import com.example.demo.entity.ApiKey;
import com.example.demo.entity.QuotaPlan;
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
    public ApiKey create(ApiKeyRequestDto dto) {

        apiKeyRepository.findByKeyValue(dto.getKeyValue())
                .ifPresent(x -> { throw new IllegalArgumentException("API key already exists: " + dto.getKeyValue()); });

        QuotaPlan plan = quotaPlanRepository.findById(dto.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("QuotaPlan not found with id: " + dto.getPlanId()));

        if (!plan.isActive()) {
            throw new IllegalArgumentException("QuotaPlan is inactive. planId=" + plan.getId());
        }

        ApiKey key = new ApiKey();
        key.setKeyValue(dto.getKeyValue());
        key.setOwnerId(dto.getOwnerId());
        key.setPlan(plan);
        key.setActive(dto.getActive() == null || dto.getActive());

        return apiKeyRepository.save(key);
    }

    @Override
    public ApiKey update(Long id, ApiKeyUpdateDto dto) {

        ApiKey existing = apiKeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found with id: " + id));

        QuotaPlan plan = quotaPlanRepository.findById(dto.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("QuotaPlan not found with id: " + dto.getPlanId()));

        if (!plan.isActive()) {
            throw new IllegalArgumentException("QuotaPlan is inactive. planId=" + plan.getId());
        }

        existing.setPlan(plan);

        if (dto.getActive() != null) {
            existing.setActive(dto.getActive());
        }

        return apiKeyRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiKey getById(Long id) {
        return apiKeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApiKey> getAll() {
        return apiKeyRepository.findAll();
    }

    @Override
    public ApiKey deactivate(Long id) {
        ApiKey existing = apiKeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found with id: " + id));
        existing.setActive(false);
        return apiKeyRepository.save(existing);
    }
}