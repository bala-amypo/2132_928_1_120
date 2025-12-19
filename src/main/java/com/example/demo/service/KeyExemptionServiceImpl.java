package com.example.demo.service;

import com.example.demo.dto.KeyExemptionRequestDto;
import com.example.demo.entity.ApiKey;
import com.example.demo.entity.KeyExemption;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.KeyExemptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeyExemptionServiceImpl implements KeyExemptionService {

    private final KeyExemptionRepository exemptionRepo;
    private final ApiKeyRepository apiKeyRepo;

    public KeyExemptionServiceImpl(KeyExemptionRepository exemptionRepo, ApiKeyRepository apiKeyRepo) {
        this.exemptionRepo = exemptionRepo;
        this.apiKeyRepo = apiKeyRepo;
    }

    @Override
    public KeyExemption createExemption(KeyExemptionRequestDto dto) {
        ApiKey apiKey = apiKeyRepo.findById(dto.getApiKeyId())
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found: " + dto.getApiKeyId()));

        KeyExemption ex = new KeyExemption();
        ex.setApiKey(apiKey);
        ex.setNotes(dto.getNotes());
        ex.setUnlimitedAccess(Boolean.TRUE.equals(dto.getUnlimitedAccess()));
        ex.setTemporaryExtensionLimit(dto.getTemporaryExtensionLimit());
        ex.setValidUntil(dto.getValidUntil());

        return exemptionRepo.save(ex);
    }

    @Override
    public KeyExemption updateExemption(Long id, KeyExemptionRequestDto dto) {
        KeyExemption ex = exemptionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KeyExemption not found: " + id));

        ApiKey apiKey = apiKeyRepo.findById(dto.getApiKeyId())
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found: " + dto.getApiKeyId()));

        ex.setApiKey(apiKey);
        ex.setNotes(dto.getNotes());
        ex.setUnlimitedAccess(Boolean.TRUE.equals(dto.getUnlimitedAccess()));
        ex.setTemporaryExtensionLimit(dto.getTemporaryExtensionLimit());
        ex.setValidUntil(dto.getValidUntil());

        return exemptionRepo.save(ex);
    }

    @Override
    public KeyExemption getExemptionByKey(Long keyId) {
        return exemptionRepo.findByApiKeyId(keyId)
                .orElseThrow(() -> new ResourceNotFoundException("Exemption not found for apiKeyId: " + keyId));
    }

    @Override
    public List<KeyExemption> getAllExemptions() {
        return exemptionRepo.findAll();
    }
}