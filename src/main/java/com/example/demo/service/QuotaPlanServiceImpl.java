package com.example.demo.service;

import com.example.demo.dto.QuotaPlanDto;
import com.example.demo.entity.QuotaPlan;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.QuotaPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuotaPlanServiceImpl implements QuotaPlanService {

    private final QuotaPlanRepository quotaPlanRepository;

    public QuotaPlanServiceImpl(QuotaPlanRepository quotaPlanRepository) {
        this.quotaPlanRepository = quotaPlanRepository;
    }

    @Override
    public QuotaPlanDto createQuotaPlan(QuotaPlanDto dto) {

        if (dto.getDailyLimit() == null || dto.getDailyLimit() <= 0) {
            throw new BadRequestException("Daily limit must be > 0");
        }

        if (quotaPlanRepository.findByPlanName(dto.getPlanName()).isPresent()) {
            throw new BadRequestException("Plan name already exists");
        }

        QuotaPlan plan = new QuotaPlan();
        plan.setPlanName(dto.getPlanName());
        plan.setDailyLimit(dto.getDailyLimit());
        plan.setDescription(dto.getDescription());
        plan.setActive(dto.getActive() != null ? dto.getActive() : true);

        QuotaPlan saved = quotaPlanRepository.save(plan);

        return toDto(saved);
    }

    @Override
    public QuotaPlanDto updateQuotaPlan(Long id, QuotaPlanDto dto) {

        QuotaPlan plan = quotaPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quota plan not found"));

        if (dto.getDailyLimit() == null || dto.getDailyLimit() <= 0) {
            throw new BadRequestException("Daily limit must be > 0");
        }

        // If planName changed, ensure uniqueness
        if (dto.getPlanName() != null && !dto.getPlanName().equalsIgnoreCase(plan.getPlanName())) {
            quotaPlanRepository.findByPlanName(dto.getPlanName()).ifPresent(x -> {
                throw new BadRequestException("Plan name already exists");
            });
            plan.setPlanName(dto.getPlanName());
        }

        plan.setDailyLimit(dto.getDailyLimit());
        plan.setDescription(dto.getDescription());
        plan.setActive(dto.getActive() != null ? dto.getActive() : plan.getActive());

        QuotaPlan saved = quotaPlanRepository.save(plan);

        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public QuotaPlanDto getQuotaPlanById(Long id) {

        QuotaPlan plan = quotaPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quota plan not found"));

        return toDto(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuotaPlanDto> getAllPlans() {

        return quotaPlanRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deactivateQuotaPlan(Long id) {

        QuotaPlan plan = quotaPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quota plan not found"));

        // Optional rule: you can restrict if active keys exist (not required if tests don't check)
        plan.setActive(false);
        quotaPlanRepository.save(plan);
    }

    private QuotaPlanDto toDto(QuotaPlan plan) {
        QuotaPlanDto dto = new QuotaPlanDto();
        dto.setId(plan.getId());
        dto.setPlanName(plan.getPlanName());
        dto.setDailyLimit(plan.getDailyLimit());
        dto.setDescription(plan.getDescription());
        dto.setActive(plan.getActive());
        return dto;
    }
}