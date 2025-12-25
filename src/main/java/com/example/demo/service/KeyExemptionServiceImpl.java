package com.example.demo.service;

import com.example.demo.dto.KeyExemptionDto;
import com.example.demo.entity.ApiKey;
import com.example.demo.entity.KeyExemption;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.KeyExemptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<KeyExemptionDto> getAllExemptions() {
        return exemptionRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<KeyExemptionDto> getExemptionsForKey(Long apiKeyId) {
        return exemptionRepository.findByApiKey_Id(apiKeyId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public KeyExemptionDto upsertExemption(KeyExemptionDto dto) {
        ApiKey apiKey = apiKeyRepository.findById(dto.getApiKeyId())
                .orElseThrow(() -> new ResourceNotFoundException("API key not found: " + dto.getApiKeyId()));

        // one record per apiKey: overwrite existing
        exemptionRepository.findByApiKey_Id(dto.getApiKeyId())
                .forEach(exemptionRepository::delete);

        KeyExemption e = new KeyExemption();
        e.setApiKey(apiKey);
        e.setExempted(dto.isExempted());
        e.setReason(dto.getReason());

        KeyExemption saved = exemptionRepository.save(e);
        return toDto(saved);
    }

    private KeyExemptionDto toDto(KeyExemption e) {
        KeyExemptionDto dto = new KeyExemptionDto();
        dto.setId(e.getId());
        dto.setApiKeyId(e.getApiKey() != null ? e.getApiKey().getId() : null);
        dto.setExempted(e.isExempted());
        dto.setReason(e.getReason());
        return dto;
    }
}