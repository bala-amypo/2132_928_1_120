package com.example.demo.service;

import com.example.demo.entity.QuotaPlan;
import com.example.demo.repository.QuotaPlanRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class QuotaPlanServiceImpl implements QuotaPlanService {

    private final QuotaPlanRepository quotaPlanRepository;

    public QuotaPlanServiceImpl(QuotaPlanRepository quotaPlanRepository) {
        this.quotaPlanRepository = quotaPlanRepository;
    }

    @Override
    public QuotaPlan createQuotaPlan(QuotaPlan plan) {
        if (plan.getDailyLimit() == null || plan.getDailyLimit() <= 0) {
            throw new RuntimeException("BadRequest: dailyLimit must be > 0");
        }
        if (plan.getActive() == null) plan.setActive(true);
        plan.setCreatedAt(Instant.now());
        plan.setUpdatedAt(Instant.now());
        return quotaPlanRepository.save(plan);
    }

    @Override
    public QuotaPlan updateQuotaPlan(Long id, QuotaPlan plan) {
        QuotaPlan existing = getQuotaPlanById(id);

        if (plan.getPlanName() != null) existing.setPlanName(plan.getPlanName());
        if (plan.getDescription() != null) existing.setDescription(plan.getDescription());
        if (plan.getActive() != null) existing.setActive(plan.getActive());

        if (plan.getDailyLimit() != null) {
            if (plan.getDailyLimit() <= 0) throw new RuntimeException("BadRequest: dailyLimit must be > 0");
            existing.setDailyLimit(plan.getDailyLimit());
        }

        existing.setUpdatedAt(Instant.now());
        return quotaPlanRepository.save(existing);
    }

    @Override
    public QuotaPlan getQuotaPlanById(Long id) {
        return quotaPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ResourceNotFound: plan not found"));
    }

    @Override
    public List<QuotaPlan> getAllPlans() {
        return quotaPlanRepository.findAll();
    }

    @Override
    public QuotaPlan deactivateQuotaPlan(Long id) {
        QuotaPlan existing = getQuotaPlanById(id);
        existing.setActive(false);
        existing.setUpdatedAt(Instant.now());
        return quotaPlanRepository.save(existing);
    }
}