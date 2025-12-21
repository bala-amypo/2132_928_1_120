package com.example.demo.service;

import com.example.demo.dto.ApiKeyRequestDto;
import com.example.demo.dto.ApiKeyResponseDto;
import com.example.demo.entity.ApiKey;
import com.example.demo.entity.QuotaPlan;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
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
    public ApiKeyResponseDto create(ApiKeyRequestDto dto) {
        if (apiKeyRepository.existsByKeyValue(dto.getKeyValue())) {
            throw new BadRequestException("keyValue already exists: " + dto.getKeyValue());
        }

        QuotaPlan plan = quotaPlanRepository.findById(dto.getPlanId())
                .orElseThrow(() -> new NotFoundException("QuotaPlan not found with id: " + dto.getPlanId()));

        ValidationRules.requireActivePlan(plan);

        ApiKey key = new ApiKey();
        key.setKeyValue(dto.getKeyValue().trim());
        key.setOwnerId(dto.getOwnerId());
        key.setPlan(plan);
        key.setActive(dto.getActive());

        ApiKey saved = apiKeyRepository.save(key);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiKeyResponseDto getById(Long id) {
        ApiKey key = apiKeyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ApiKey not found with id: " + id));
        return toDto(key);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApiKeyResponseDto> getAll() {
        return apiKeyRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public ApiKeyResponseDto update(Long id, ApiKeyRequestDto dto) {
        ApiKey existing = apiKeyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ApiKey not found with id: " + id));

        // keyValue uniqueness check (if changed)
        String newKeyValue = dto.getKeyValue().trim();
        if (!existing.getKeyValue().equals(newKeyValue)) {
            apiKeyRepository.findByKeyValue(newKeyValue).ifPresent(k -> {
                throw new BadRequestException("keyValue already exists: " + newKeyValue);
            });
            existing.setKeyValue(newKeyValue);
        }

        QuotaPlan plan = quotaPlanRepository.findById(dto.getPlanId())
                .orElseThrow(() -> new NotFoundException("QuotaPlan not found with id: " + dto.getPlanId()));

        ValidationRules.requireActivePlan(plan);

        existing.setOwnerId(dto.getOwnerId());
        existing.setPlan(plan);
        existing.setActive(dto.getActive());

        ApiKey saved = apiKeyRepository.save(existing);
        return toDto(saved);
    }

    @Override
    public ApiKeyResponseDto deactivate(Long id) {
        ApiKey existing = apiKeyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ApiKey not found with id: " + id));

        existing.setActive(false);
        ApiKey saved = apiKeyRepository.save(existing);
        return toDto(saved);
    }

    private ApiKeyResponseDto toDto(ApiKey key) {
        QuotaPlan p = key.getPlan();
        return new ApiKeyResponseDto(
                key.getId(),
                key.getKeyValue(),
                key.getOwnerId(),
                p != null ? p.getId() : null,
                p != null ? p.getPlanName() : null,
                key.getActive(),
                key.getCreatedAt(),
                key.getUpdatedAt()
        );
    }
}