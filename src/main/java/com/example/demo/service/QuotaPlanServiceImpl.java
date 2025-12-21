package com.example.demo.service;

import com.example.demo.dto.QuotaPlanRequestDto;
import com.example.demo.entity.QuotaPlan;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ApiKeyRepository;
import com.example.demo.repository.QuotaPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class QuotaPlanServiceImpl implements QuotaPlanService {

    private final QuotaPlanRepository quotaPlanRepository;
    private final ApiKeyRepository apiKeyRepository;

    public QuotaPlanServiceImpl(QuotaPlanRepository quotaPlanRepository, ApiKeyRepository apiKeyRepository) {
        this.quotaPlanRepository = quotaPlanRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public QuotaPlan create(QuotaPlanRequestDto dto) {
        String name = dto.getPlanName().trim();

        if (quotaPlanRepository.existsByPlanNameIgnoreCase(name)) {
            throw new BadRequestException("Plan name already exists: " + name);
        }

        QuotaPlan plan = QuotaPlan.builder()
                .planName(name)
                .dailyLimit(dto.getDailyLimit())
                .description(dto.getDescription() == null ? null : dto.getDescription().trim())
                .active(dto.getActive())
                .build();

        return quotaPlanRepository.save(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public QuotaPlan getById(Long id) {
        return quotaPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuotaPlan not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuotaPlan> getAll() {
        return quotaPlanRepository.findAll();
    }

    @Override
    public QuotaPlan update(Long id, QuotaPlanRequestDto dto) {
        QuotaPlan existing = getById(id);

        String newName = dto.getPlanName().trim();

        // uniqueness only if name changed
        if (!existing.getPlanName().equalsIgnoreCase(newName)
                && quotaPlanRepository.existsByPlanNameIgnoreCase(newName)) {
            throw new BadRequestException("Plan name already exists: " + newName);
        }

        existing.setPlanName(newName);
        existing.setDailyLimit(dto.getDailyLimit());
        existing.setDescription(dto.getDescription() == null ? null : dto.getDescription().trim());
        existing.setActive(dto.getActive());

        return quotaPlanRepository.save(existing);
    }

    @Override
    public QuotaPlan deactivate(Long id) {
        QuotaPlan existing = getById(id);
        existing.setActive(false);
        return quotaPlanRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        QuotaPlan existing = getById(id);

        long keysUsingThisPlan = apiKeyRepository.countByPlan_Id(id);
        if (keysUsingThisPlan > 0) {
            // safer than deleting
            throw new BadRequestException("Cannot delete plan. ApiKeys are linked to this plan: " + keysUsingThisPlan);
        }

        quotaPlanRepository.delete(existing);
    }
}