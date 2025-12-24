package com.example.demo.service;

import com.example.demo.dto.KeyExemptionDto;
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
import java.util.stream.Collectors;

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
    public KeyExemptionDto createExemption(KeyExemptionDto dto) {

        ApiKey apiKey = apiKeyRepository.findById(dto.getApiKeyId())
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found"));

        if (!Boolean.TRUE.equals(apiKey.getActive())) {
            throw new BadRequestException("ApiKey is inactive");
        }

        validateExemption(dto);

        KeyExemption exemption = new KeyExemption();
        exemption.setApiKey(apiKey);
        exemption.setNotes(dto.getNotes());
        exemption.setUnlimitedAccess(dto.getUnlimitedAccess() != null && dto.getUnlimitedAccess());
        exemption.setTemporaryExtensionLimit(dto.getTemporaryExtensionLimit());
        exemption.setValidUntil(dto.getValidUntil());

        return toDto(exemptionRepository.save(exemption));
    }

    @Override
    public KeyExemptionDto updateExemption(Long id, KeyExemptionDto dto) {

        KeyExemption exemption = exemptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exemption not found"));

        validateExemption(dto);

        exemption.setNotes(dto.getNotes());
        exemption.setUnlimitedAccess(dto.getUnlimitedAccess());
        exemption.setTemporaryExtensionLimit(dto.getTemporaryExtensionLimit());
        exemption.setValidUntil(dto.getValidUntil());

        return toDto(exemptionRepository.save(exemption));
    }

    @Override
    @Transactional(readOnly = true)
    public KeyExemptionDto getExemptionByKey(Long apiKeyId) {

        KeyExemption exemption = exemptionRepository.findByApiKeyId(apiKeyId)
                .orElseThrow(() -> new ResourceNotFoundException("Exemption not found"));

        return toDto(exemption);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KeyExemptionDto> getAllExemptions() {

        return exemptionRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private void validateExemption(KeyExemptionDto dto) {

        if (dto.getUnlimitedAccess() != null && dto.getUnlimitedAccess()
                && dto.getTemporaryExtensionLimit() != null) {
            throw new BadRequestException("Cannot have both unlimited and temporary extension");
        }

        if (dto.getTemporaryExtensionLimit() != null && dto.getTemporaryExtensionLimit() < 0) {
            throw new BadRequestException("Temporary extension must be >= 0");
        }

        if (dto.getValidUntil() == null || dto.getValidUntil().isBefore(Instant.now())) {
            throw new BadRequestException("validUntil must be in the future");
        }
    }

    private KeyExemptionDto toDto(KeyExemption e) {
        KeyExemptionDto dto = new KeyExemptionDto();
        dto.setId(e.getId());
        dto.setApiKeyId(e.getApiKey().getId());
        dto.setNotes(e.getNotes());
        dto.setUnlimitedAccess(e.getUnlimitedAccess());
        dto.setTemporaryExtensionLimit(e.getTemporaryExtensionLimit());
        dto.setValidUntil(e.getValidUntil());
        return dto;
    }
}