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
public class KeyExemptionServiceImpl implements KeyExemptionService {

    private final KeyExemptionRepository keyExemptionRepository;
    private final ApiKeyRepository apiKeyRepository;

    public KeyExemptionServiceImpl(KeyExemptionRepository keyExemptionRepository,
                                   ApiKeyRepository apiKeyRepository) {
        this.keyExemptionRepository = keyExemptionRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    @Transactional
    public KeyExemptionResponseDto create(KeyExemptionRequestDto dto) {

        ApiKey apiKey = apiKeyRepository.findById(dto.getApiKeyId())
                .orElseThrow(() ->
                        new NotFoundException("ApiKey not found with id: " + dto.getApiKeyId()));

        KeyExemption ex = new KeyExemption();
        ex.setApiKey(apiKey);
        ex.setNotes(trim(dto.getNotes()));
        ex.setUnlimitedAccess(Boolean.TRUE.equals(dto.getUnlimitedAccess()));
        ex.setTemporaryExtensionLimit(dto.getTemporaryExtensionLimit());
        ex.setValidUntil(dto.getValidUntil());

        validate(ex);

        Instant now = Instant.now();
        ex.setCreatedAt(now);
        ex.setUpdatedAt(now);

        KeyExemption saved = keyExemptionRepository.save(ex);

        // 🔴 Rollback demo (like Student/AIML example)
        if ("AIML".equalsIgnoreCase(saved.getNotes())) {
            throw new NotFoundException("Testing Transaction Rollback");
        }

        return toDto(saved);
    }

    @Override
    @Transactional
    public KeyExemptionResponseDto update(Long id, KeyExemptionRequestDto dto) {

        KeyExemption existing = keyExemptionRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("KeyExemption not found with id: " + id));

        ApiKey apiKey = apiKeyRepository.findById(dto.getApiKeyId())
                .orElseThrow(() ->
                        new NotFoundException("ApiKey not found with id: " + dto.getApiKeyId()));

        existing.setApiKey(apiKey);
        existing.setNotes(trim(dto.getNotes()));
        existing.setUnlimitedAccess(Boolean.TRUE.equals(dto.getUnlimitedAccess()));
        existing.setTemporaryExtensionLimit(dto.getTemporaryExtensionLimit());
        existing.setValidUntil(dto.getValidUntil());

        validate(existing);

        existing.setUpdatedAt(Instant.now());

        return toDto(keyExemptionRepository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public List<KeyExemptionResponseDto> getAllExemptions() {
        return keyExemptionRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<KeyExemptionResponseDto> getByApiKeyId(Long apiKeyId) {
        return keyExemptionRepository.findByApiKeyHql(apiKeyId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public KeyExemptionResponseDto getLatestByApiKeyId(Long apiKeyId) {
        KeyExemption ex = keyExemptionRepository.findLatestByApiKeyHql(apiKeyId)
                .orElseThrow(() ->
                        new NotFoundException("No exemption found for apiKeyId: " + apiKeyId));
        return toDto(ex);
    }

    @Override
    @Transactional(readOnly = true)
    public KeyExemptionResponseDto getById(Long id) {
        return keyExemptionRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() ->
                        new NotFoundException("KeyExemption not found with id: " + id));
    }

    /* ------------------ helpers ------------------ */

    private void validate(KeyExemption ex) {
        if (ex.getTemporaryExtensionLimit() != null && ex.getTemporaryExtensionLimit() < 0) {
            throw new BadRequestException("temporaryExtensionLimit must be >= 0");
        }
        if (ex.getValidUntil()