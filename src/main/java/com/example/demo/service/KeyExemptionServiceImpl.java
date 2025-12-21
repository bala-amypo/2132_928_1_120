package com.example.demo.service;

import com.example.demo.dto.KeyExemptionRequestDto;
import com.example.demo.entity.ApiKey;
import com.example.demo.entity.KeyExemption;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
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
    public KeyExemption createExemption(KeyExemptionRequestDto dto) {
        ApiKey apiKey = apiKeyRepository.findById(dto.getApiKeyId())
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found with id: " + dto.getApiKeyId()));

        // Only one exemption per ApiKey
        keyExemptionRepository.findByApiKey_Id(apiKey.getId()).ifPresent(ex -> {
            throw new BadRequestException("Exemption already exists for ApiKey id: " + apiKey.getId());
        });

        validateValidUntil(dto.getValidUntil());

        KeyExemption ex = new KeyExemption();
        ex.setApiKey(apiKey);
        ex.setNotes(trim(dto.getNotes()));
        ex.setUnlimitedAccess(dto.getUnlimitedAccess());
        ex.setTemporaryExtensionLimit(dto.getTemporaryExtensionLimit());
        ex.setValidUntil(dto.getValidUntil());

        return keyExemptionRepository.save(ex);
    }

    @Override
    public KeyExemption updateExemption(Long id, KeyExemptionRequestDto dto) {
        KeyExemption existing = keyExemptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KeyExemption not found with id: " + id));

        // If ApiKey changes, ensure key exists & no other exemption uses it
        if (dto.getApiKeyId() != null && !dto.getApiKeyId().equals(existing.getApiKey().getId())) {
            ApiKey newKey = apiKeyRepository.findById(dto.getApiKeyId())
                    .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found with id: " + dto.getApiKeyId()));

            keyExemptionRepository.findByApiKey_Id(newKey.getId()).ifPresent(other -> {
                if (!other.getId().equals(existing.getId())) {
                    throw new BadRequestException("Another exemption already exists for ApiKey id: " + newKey.getId());
                }
            });

            existing.setApiKey(newKey);
        }

        if (dto.getNotes() != null) existing.setNotes(trim(dto.getNotes()));
        if (dto.getUnlimitedAccess() != null) existing.setUnlimitedAccess(dto.getUnlimitedAccess());
        if (dto.getTemporaryExtensionLimit() != null) existing.setTemporaryExtensionLimit(dto.getTemporaryExtensionLimit());

        if (dto.getValidUntil() != null) {
            validateValidUntil(dto.getValidUntil());
            existing.setValidUntil(dto.getValidUntil());
        }

        return keyExemptionRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public KeyExemption getExemptionByKey(Long apiKeyId) {
        return keyExemptionRepository.findByApiKey_Id(apiKeyId)
                .orElseThrow(() -> new ResourceNotFoundException("Exemption not found for ApiKey id: " + apiKeyId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<KeyExemption> getAllExemptions() {
        return keyExemptionRepository.findAll();
    }

    private static void validateValidUntil(Instant validUntil) {
        if (validUntil == null) throw new BadRequestException("validUntil is required");
        if (!validUntil.isAfter(Instant.now())) throw new BadRequestException("validUntil must be in the future");
    }

    private static String trim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}