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
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final QuotaPlanRepository quotaPlanRepository;

    public ApiKeyServiceImpl(ApiKeyRepository apiKeyRepository,
                             QuotaPlanRepository quotaPlanRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.quotaPlanRepository = quotaPlanRepository;
    }

    @Override
    @Transactional
    public ApiKeyResponseDto create(ApiKeyRequestDto dto) {

        if (apiKeyRepository.existsByKeyValue(dto.getKeyValue())) {
            throw new BadRequestException("keyValue already exists: " + dto.getKeyValue());
        }

        QuotaPlan plan = quotaPlanRepository.findById(dto.getPlanId())
                .orElseThrow(() ->
                        new NotFoundException("QuotaPlan not found with id: " + dto.getPlanId())
                );

        ApiKey key = new ApiKey();
        key.setKeyValue(dto.getKeyValue().trim());
        key.setOwnerId(dto.getOwnerId());
        key.setPlan(plan);
        key.setActive(dto.getActive());

        // ✅ SAVE FIRST (same style as studentRepository.save(student))
        ApiKey saved = apiKeyRepository.save(key);

        // ✅ THEN CONDITION + THROW (same style as AIML check)
        if (saved.getOwnerId() != null && saved.getOwnerId().equals(999L)) {
            throw new BadRequestException("Testing");
        }

        // ✅ RETURN SAVED
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
    @Transactional
    public ApiKeyResponseDto update(Long id, ApiKeyRequestDto dto) {

        ApiKey existing = apiKeyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ApiKey not found with id: " + id));

        String newKeyValue = dto.getKeyValue().trim();
        if (!existing.getKeyValue().equals(newKeyValue)) {
            apiKeyRepository.findByKeyValue(newKeyValue).ifPresent(k -> {
                throw new BadRequestException("keyValue already exists: " + newKeyValue);
            });
            existing.setKeyValue(newKeyValue);
        }

        QuotaPlan plan = quotaPlanRepository.findById(dto.getPlanId())
                .orElseThrow(() -> new NotFoundException("QuotaPlan not found with id: " + dto.getPlanId()));

        existing.setOwnerId(dto.getOwnerId());
        existing.setPlan(plan);
        existing.setActive(dto.getActive());

        ApiKey saved = apiKeyRepository.save(existing);
        return toDto(saved);
    }

    @Override
    @Transactional
    public ApiKeyResponseDto deactivate(Long id) {

        ApiKey key = apiKeyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ApiKey not found with id: " + id));

        key.setActive(false);
        ApiKey saved = apiKeyRepository.save(key);
        return toDto(saved);
    }

    private ApiKeyResponseDto toDto(ApiKey key) {
        QuotaPlan plan = key.getPlan();

        return new ApiKeyResponseDto(
                key.getId(),
                key.getKeyValue(),
                key.getOwnerId(),
                plan != null ? plan.getId() : null,
                plan != null ? plan.getPlanName() : null,
                key.getActive(),
                key.getCreatedAt(),
                key.getUpdatedAt()
        );
    }
}