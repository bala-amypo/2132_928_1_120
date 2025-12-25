package com.example.demo.service;

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

    private final KeyExemptionRepository exemptionRepository;
    private final ApiKeyRepository apiKeyRepository;

    public KeyExemptionServiceImpl(KeyExemptionRepository exemptionRepository,
                                   ApiKeyRepository apiKeyRepository) {
        this.exemptionRepository = exemptionRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public KeyExemption createExemption(KeyExemption exemption) {
        if (exemption == null) throw new BadRequestException("Exemption is required");
        if (exemption.getApiKey() == null || exemption.getApiKey().getId() == null) {
            throw new BadRequestException("ApiKey is required");
        }

        ApiKey apiKey = apiKeyRepository.findById(exemption.getApiKey().getId())
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found"));

        if (!apiKey.isActive()) throw new BadRequestException("ApiKey is inactive");

        validate(exemption);

        exemption.setApiKey(apiKey);
        return exemptionRepository.save(exemption);
    }

    @Override
    public KeyExemption updateExemption(Long id, KeyExemption input) {
        KeyExemption ex = exemptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exemption not found"));

        validate(input);

        ex.setNotes(input.getNotes());
        ex.setUnlimitedAccess(input.getUnlimitedAccess());
        ex.setTemporaryExtensionLimit(input.getTemporaryExtensionLimit());
        ex.setValidUntil(input.getValidUntil());

        return exemptionRepository.save(ex);
    }

    @Override
    @Transactional(readOnly = true)
    public KeyExemption getExemptionByKey(Long apiKeyId) {
        return exemptionRepository.findByApiKeyId(apiKeyId)
                .orElseThrow(() -> new ResourceNotFoundException("Exemption not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<KeyExemption> getAllExemptions() {
        return exemptionRepository.findAll();
    }

    private void validate(KeyExemption ex) {
        Boolean unlimited = ex.getUnlimitedAccess() != null && ex.getUnlimitedAccess();
        Integer temp = ex.getTemporaryExtensionLimit();

        if (unlimited && temp != null) {
            throw new BadRequestException("Cannot have both unlimited and temporary extension");
        }
        if (temp != null && temp < 0) {
            throw new BadRequestException("Temporary extension must be >= 0");
        }
        if (ex.getValidUntil() == null || ex.getValidUntil().isBefore(Instant.now())) {
            throw new BadRequestException("validUntil must be in the future");
        }
    }
}