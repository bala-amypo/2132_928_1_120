package com.example.demo.service;

import com.example.demo.entity.ApiKey;
import com.example.demo.entity.KeyExemption;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.KeyExemptionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class KeyExemptionServiceImpl implements KeyExemptionService {

    private final KeyExemptionRepository keyExemptionRepository;
    private final ApiKeyRepository apiKeyRepository;

    public KeyExemptionServiceImpl(KeyExemptionRepository keyExemptionRepository, ApiKeyRepository apiKeyRepository) {
        this.keyExemptionRepository = keyExemptionRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public KeyExemption createExemption(KeyExemption exemption) {
        validate(exemption);

        ApiKey key = apiKeyRepository.findById(exemption.getApiKey().getId())
                .orElseThrow(() -> new RuntimeException("ResourceNotFound: api key not found"));
        exemption.setApiKey(key);
        return keyExemptionRepository.save(exemption);
    }

    @Override
    public KeyExemption updateExemption(Long id, KeyExemption exemption) {
        KeyExemption existing = keyExemptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ResourceNotFound: exemption not found"));

        if (exemption.getNotes() != null) existing.setNotes(exemption.getNotes());
        if (exemption.getUnlimitedAccess() != null) existing.setUnlimitedAccess(exemption.getUnlimitedAccess());
        if (exemption.getTemporaryExtensionLimit() != null) existing.setTemporaryExtensionLimit(exemption.getTemporaryExtensionLimit());
        if (exemption.getValidUntil() != null) existing.setValidUntil(exemption.getValidUntil());

        validate(existing);
        return keyExemptionRepository.save(existing);
    }

    @Override
    public KeyExemption getExemptionByKey(Long apiKeyId) {
        return keyExemptionRepository.findByApiKey_Id(apiKeyId).stream().findFirst()
                .orElseThrow(() -> new RuntimeException("ResourceNotFound: exemption not found for key"));
    }

    @Override
    public List<KeyExemption> getAllExemptions() {
        return keyExemptionRepository.findAll();
    }

    private void validate(KeyExemption ex) {
        if (ex.getApiKey() == null || ex.getApiKey().getId() == null) {
            throw new RuntimeException("BadRequest: apiKey is required");
        }
        if (ex.getTemporaryExtensionLimit() != null && ex.getTemporaryExtensionLimit() < 0) {
            throw new RuntimeException("BadRequest: temporaryExtensionLimit must be >= 0");
        }
        if (ex.getValidUntil() != null && !ex.getValidUntil().isAfter(Instant.now())) {
            throw new RuntimeException("BadRequest: validUntil must be in the future");
        }
        if (ex.getUnlimitedAccess() == null) ex.setUnlimitedAccess(false);
    }
}