package com.example.demo.service;

import com.example.demo.entity.QuotaPlan;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.QuotaPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class QuotaPlanServiceImpl implements QuotaPlanService {

    private final QuotaPlanRepository quotaPlanRepository;

    public QuotaPlanServiceImpl(QuotaPlanRepository quotaPlanRepository) {
        this.quotaPlanRepository = quotaPlanRepository;
    }

    @Override
    public QuotaPlan createQuotaPlan(QuotaPlan plan) {
        if (plan == null) throw new BadRequestException("Plan is required");
        if (plan.getDailyLimit() == null || plan.getDailyLimit() <= 0) {
            throw new BadRequestException("Daily limit must be > 0");
        }
        if (plan.getPlanName() == null || plan.getPlanName().trim().isEmpty()) {
            throw new BadRequestException("Plan name is required");
        }

        quotaPlanRepository.findByPlanName(plan.getPlanName().trim())
                .ifPresent(x -> { throw new BadRequestException("Plan name already exists"); });

        plan.setPlanName(plan.getPlanName().trim());
        if (plan.getActive() == null) plan.setActive(true);

        return quotaPlanRepository.save(plan);
    }

    @Override
    public QuotaPlan updateQuotaPlan(Long id, QuotaPlan input) {
        QuotaPlan plan = quotaPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quota plan not found"));

        if (input.getDailyLimit() == null || input.getDailyLimit() <= 0) {
            throw new BadRequestException("Daily limit must be > 0");
        }

        if (input.getPlanName() != null && !input.getPlanName().equalsIgnoreCase(plan.getPlanName())) {
            quotaPlanRepository.findByPlanName(input.getPlanName().trim())
                    .ifPresent(x -> { throw new BadRequestException("Plan name already exists"); });
            plan.setPlanName(input.getPlanName().trim());
        }

        plan.setDailyLimit(input.getDailyLimit());
        plan.setDescription(input.getDescription());
        if (input.getActive() != null) plan.setActive(input.getActive());

        return quotaPlanRepository.save(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public QuotaPlan getQuotaPlanById(Long id) {
        return quotaPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quota plan not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuotaPlan> getAllPlans() {
        return quotaPlanRepository.findAll();
    }

    @Override
    public void deactivateQuotaPlan(Long id) {
        QuotaPlan plan = quotaPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quota plan not found"));
        plan.setActive(false);
        quotaPlanRepository.save(plan);
    }
}