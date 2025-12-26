package com.example.demo.service.impl;

import com.example.demo.entity.ApiKey;
import com.example.demo.entity.KeyExemption;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.KeyExemptionRepository;
import com.example.demo.service.KeyExemptionService;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    public KeyExemption createExemption(KeyExemption ex) {
        if (ex == null) throw new BadRequestException("Exemption payload is required");
        if (ex.getApiKey() == null || ex.getApiKey().getId() == null) {
            throw new BadRequestException("ApiKey id is required");
        }

        Long apiKeyId = ex.getApiKey().getId();

        ApiKey key = apiKeyRepo.findById(apiKeyId)
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found: " + apiKeyId));

        if (ex.getTemporaryExtensionLimit() < 0) {
            throw new BadRequestException("temporaryExtensionLimit must be >= 0");
        }

        if (ex.getValidUntil() != null && ex.getValidUntil().isBefore(Instant.now())) {
            throw new BadRequestException("validUntil must be in the future");
        }

        ex.setApiKey(key);
        return exemptionRepo.save(ex);
    }

    @Override
    public KeyExemption updateExemption(Long apiKeyId, KeyExemption ex) {
        if (apiKeyId == null) throw new BadRequestException("apiKeyId is required");
        if (ex == null) throw new BadRequestException("Exemption payload is required");

        KeyExemption existing = exemptionRepo.findByApiKey_Id(apiKeyId)
                .orElseThrow(() -> new ResourceNotFoundException("Exemption not found for apiKeyId: " + apiKeyId));

        if (ex.getTemporaryExtensionLimit() < 0) {
            throw new BadRequestException("temporaryExtensionLimit must be >= 0");
        }
        if (ex.getValidUntil() != null && ex.getValidUntil().isBefore(Instant.now())) {
            throw new BadRequestException("validUntil must be in the future");
        }

        // update allowed fields
        existing.setTemporaryExtensionLimit(ex.getTemporaryExtensionLimit());
        existing.setValidUntil(ex.getValidUntil());
        existing.setReason(ex.getReason());

        return exemptionRepo.save(existing);
    }

    @Override
    public KeyExemption getExemptionByKey(Long apiKeyId) {
        if (apiKeyId == null) throw new BadRequestException("apiKeyId is required");
        return exemptionRepo.findByApiKey_Id(apiKeyId)
                .orElseThrow(() -> new ResourceNotFoundException("Exemption not found for apiKeyId: " + apiKeyId));
    }

    @Override
    public List<KeyExemption> getAllExemptions() {
        return exemptionRepo.findAll();
    }
}