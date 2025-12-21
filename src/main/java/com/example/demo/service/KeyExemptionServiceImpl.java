// src/main/java/com/example/demo/service/KeyExemptionServiceImpl.java
package com.example.demo.service;

import com.example.demo.dto.KeyExemptionRequestDto;
import com.example.demo.dto.KeyExemptionResponseDto;
import com.example.demo.entity.ApiKey;
import com.example.demo.entity.KeyExemption;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.KeyExemptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class KeyExemptionServiceImpl implements KeyExemptionService {

    private final KeyExemptionRepository keyExemptionRepository;
    private final ApiKeyRepository apiKeyRepository;

    public KeyExemptionServiceImpl(KeyExemptionRepository keyExemptionRepository,
                                   ApiKeyRepository apiKeyRepository) {
        this.keyExemptionRepository = keyExemptionRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public KeyExemptionResponseDto create(KeyExemptionRequestDto dto) {
        ApiKey apiKey = apiKeyRepository.findById(dto.getApiKeyId())
                .orElseThrow(() -> new NotFoundException("ApiKey not found with id: " + dto.getApiKeyId()));

        KeyExemption ex = new KeyExemption();
        ex.setApiKey(apiKey);
        ex.setNotes(safeTrim(dto.getNotes()));
        ex.setUnlimitedAccess(Boolean.TRUE.equals(dto.getUnlimitedAccess()));
        ex.setTemporaryExtensionLimit(dto.getTemporaryExtensionLimit());
        ex.setValidUntil(dto.getValidUntil());

        validateExemption(ex);

        Instant now = Instant.now();
        ex.setCreatedAt(now);
        ex.setUpdatedAt(now);

        KeyExemption saved = keyExemptionRepository.save(ex);
        return toDto(saved);
    }

    @Override
    public KeyExemptionResponseDto update(Long id, KeyExemptionRequestDto dto) {
        KeyExemption existing = keyExemptionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("KeyExemption not found with id: " + id));

        ApiKey apiKey = apiKeyRepository.findById(dto.getApiKeyId())
                .orElseThrow(() -> new NotFoundException("ApiKey not found with id: " + dto.getApiKeyId()));

        existing.setApiKey(apiKey);
        existing.setNotes(safeTrim(dto.getNotes()));
        existing.setUnlimitedAccess(Boolean.TRUE.equals(dto.getUnlimitedAccess()));
        existing.setTemporaryExtensionLimit(dto.getTemporaryExtensionLimit());
        existing.setValidUntil(dto.getValidUntil());

        validateExemption(existing);

        existing.setUpdatedAt(Instant.now());
        return toDto(keyExemptionRepository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public List<KeyExemptionResponseDto> getAllExemptions() {
        return keyExemptionRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<KeyExemptionResponseDto> getByApiKeyId(Long apiKeyId) {
        return keyExemptionRepository.findByApiKey_Id(apiKeyId).stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public KeyExemptionResponseDto getById(Long id) {
        return keyExemptionRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new NotFoundException("KeyExemption not found with id: " + id));
    }

    // ---------------- helpers ----------------

    private KeyExemptionResponseDto toDto(KeyExemption ex) {
        return new KeyExemptionResponseDto(
                ex.getId(),
                ex.getApiKey() != null ? ex.getApiKey().getId() : null,
                ex.getNotes(),
                ex.getUnlimitedAccess(),
                ex.getTemporaryExtensionLimit(),
                ex.getValidUntil(),
                ex.getCreatedAt(),
                ex.getUpdatedAt()
        );
    }

    private void validateExemption(KeyExemption ex) {
        if (ex.getTemporaryExtensionLimit() != null && ex.getTemporaryExtensionLimit() < 0) {
            throw new BadRequestException("temporaryExtensionLimit must be >= 0");
        }
        if (ex.getValidUntil() != null && !ex.getValidUntil().isAfter(Instant.now())) {
            throw new BadRequestException("validUntil must be in the future");
        }
        // optional: if unlimited, ignore extensionLimit
        if (Boolean.TRUE.equals(ex.getUnlimitedAccess())) {
            // you can choose to null it out
            // ex.setTemporaryExtensionLimit(null);
        }
    }

    private String safeTrim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}