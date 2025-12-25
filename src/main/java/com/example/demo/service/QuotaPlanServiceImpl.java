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

        if (dto == null) throw new BadRequestException("Request is required");

        if (dto.getPlanName() == null || dto.getPlanName().trim().isEmpty()) {
            throw new BadRequestException("Plan name is required");
        }

        if (dto.getDailyLimit() == null || dto.getDailyLimit() <= 0) {
            throw new BadRequestException("Daily limit must be > 0");
        }

        quotaPlanRepository.findByPlanName(dto.getPlanName().trim())
                .ifPresent(x -> { throw new BadRequestException("Plan name already exists"); });

        QuotaPlan plan = new QuotaPlan();
        plan.setPlanName(dto.getPlanName().trim());
        plan.setDailyLimit(dto.getDailyLimit());
        plan.setDescription(dto.getDescription());
        plan.setActive(dto.getActive() != null ? dto.getActive() : true);

        return toDto(quotaPlanRepository.save(plan));
    }

    @Override
    public QuotaPlanDto updateQuotaPlan(Long id, QuotaPlanDto dto) {

        if (dto == null) throw new BadRequestException("Request is required");

        QuotaPlan plan = quotaPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quota plan not found"));

        if (dto.getPlanName() != null && !dto.getPlanName().trim().isEmpty()
                && !dto.getPlanName().trim().equalsIgnoreCase(plan.getPlanName())) {

            quotaPlanRepository.findByPlanName(dto.getPlanName().trim())
                    .ifPresent(x -> { throw new BadRequestException("Plan name already exists"); });

            plan.setPlanName(dto.getPlanName().trim());
        }

        if (dto.getDailyLimit() == null || dto.getDailyLimit() <= 0) {
            throw new BadRequestException("Daily limit must be > 0");
        }

        plan.setDailyLimit(dto.getDailyLimit());
        plan.setDescription(dto.getDescription());

        if (dto.getActive() != null) {
            plan.setActive(dto.getActive());
        }

        return toDto(quotaPlanRepository.save(plan));
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