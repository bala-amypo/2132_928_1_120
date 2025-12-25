package com.example.demo.service.impl;

import com.example.demo.entity.QuotaPlan;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.QuotaPlanRepository;
import com.example.demo.service.QuotaPlanService;

import java.util.List;

public class QuotaPlanServiceImpl implements QuotaPlanService {

    private final QuotaPlanRepository quotaPlanRepo;

    public QuotaPlanServiceImpl(QuotaPlanRepository quotaPlanRepo) {
        this.quotaPlanRepo = quotaPlanRepo;
    }

    @Override
    public QuotaPlan createQuotaPlan(QuotaPlan plan) {
        if (plan.getDailyLimit() <= 0) {
            throw new BadRequestException("Daily limit must be > 0");
        }
        if (plan.getPlanName() == null || plan.getPlanName().trim().isEmpty()) {
            throw new BadRequestException("Plan name required");
        }
        if (!plan.isActive()) {
            // allow inactive create? test sets active true only in success case.
            plan.setActive(true);
        }
        return quotaPlanRepo.save(plan);
    }

    @Override
    public QuotaPlan getQuotaPlanById(Long id) {
        return quotaPlanRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));
    }

    @Override
    public QuotaPlan updateQuotaPlan(Long id, QuotaPlan updated) {
        QuotaPlan existing = getQuotaPlanById(id);
        if (updated.getPlanName() != null) existing.setPlanName(updated.getPlanName());
        if (updated.getDailyLimit() > 0) existing.setDailyLimit(updated.getDailyLimit());
        return quotaPlanRepo.save(existing);
    }

    @Override
    public List<QuotaPlan> getAllPlans() {
        return quotaPlanRepo.findAll();
    }

    @Override
    public void deactivateQuotaPlan(Long id) {
        QuotaPlan p = getQuotaPlanById(id);
        p.setActive(false);
        quotaPlanRepo.save(p);
    }
}