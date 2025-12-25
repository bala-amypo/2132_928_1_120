package com.example.demo.service.impl;

import com.example.demo.entity.ApiKey;
import com.example.demo.entity.KeyExemption;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.KeyExemptionRepository;
import com.example.demo.service.KeyExemptionService;

public class KeyExemptionServiceImpl implements KeyExemptionService {

    private final KeyExemptionRepository exemptionRepo;
    private final ApiKeyRepository apiKeyRepo;

    public KeyExemptionServiceImpl(KeyExemptionRepository exemptionRepo, ApiKeyRepository apiKeyRepo) {
        this.exemptionRepo = exemptionRepo;
        this.apiKeyRepo = apiKeyRepo;
    }

    @Override
    public KeyExemption createExemption(KeyExemption ex) {
        if (ex.getApiKey() == null || ex.getApiKey().getId() == null) {
            throw new BadRequestException("ApiKey required");
        }
        ApiKey k = apiKeyRepo.findById(ex.getApiKey().getId())
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found"));

        if (ex.getTemporaryExtensionLimit() < 0) {
            // test expects -1 fails
            throw new BadRequestException("temporaryExtensionLimit must be >= 0");
        }

        ex.setApiKey(k);
        return exemptionRepo.save(ex);
    }

    @Override
    public KeyExemption getExemptionByKey(Long apiKeyId) {
        return exemptionRepo.findByApiKey_Id(apiKeyId)
                .orElseThrow(() -> new ResourceNotFoundException("Exemption not found"));
    }
}package com.example.demo.service.impl;

import com.example.demo.entity.ApiKey;
import com.example.demo.entity.KeyExemption;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.KeyExemptionRepository;
import com.example.demo.service.KeyExemptionService;

public class KeyExemptionServiceImpl implements KeyExemptionService {

    private final KeyExemptionRepository exemptionRepo;
    private final ApiKeyRepository apiKeyRepo;

    public KeyExemptionServiceImpl(KeyExemptionRepository exemptionRepo, ApiKeyRepository apiKeyRepo) {
        this.exemptionRepo = exemptionRepo;
        this.apiKeyRepo = apiKeyRepo;
    }

    @Override
    public KeyExemption createExemption(KeyExemption ex) {
        if (ex.getApiKey() == null || ex.getApiKey().getId() == null) {
            throw new BadRequestException("ApiKey required");
        }
        ApiKey k = apiKeyRepo.findById(ex.getApiKey().getId())
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found"));

        if (ex.getTemporaryExtensionLimit() < 0) {
            // test expects -1 fails
            throw new BadRequestException("temporaryExtensionLimit must be >= 0");
        }

        ex.setApiKey(k);
        return exemptionRepo.save(ex);
    }

    @Override
    public KeyExemption getExemptionByKey(Long apiKeyId) {
        return exemptionRepo.findByApiKey_Id(apiKeyId)
                .orElseThrow(() -> new ResourceNotFoundException("Exemption not found"));
    }
}