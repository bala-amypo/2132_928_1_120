package com.example.demo.service.impl;

import com.example.demo.entity.QuotaPlan;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.QuotaPlanRepository;
import com.example.demo.service.QuotaPlanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service  // ✅ THIS is what creates the Spring bean
@Transactional
public class QuotaPlanServiceImpl implements QuotaPlanService {

    private final QuotaPlanRepository quotaPlanRepository;

    public QuotaPlanServiceImpl(QuotaPlanRepository quotaPlanRepository) {
        this.quotaPlanRepository = quotaPlanRepository;
    }

    @Override
    public QuotaPlan createQuotaPlan(QuotaPlan plan) {
        if (plan == null) throw new BadRequestException("Plan cannot be null");
        if (plan.getPlanName() == null || plan.getPlanName().trim().isEmpty())
            throw new BadRequestException("Plan name is required");
        if (plan.getDailyLimit() <= 0)
            throw new BadRequestException("Daily limit must be > 0");

        // default active if not set
        // (boolean primitive defaults to false; set true if you want)
        // if you want: plan.setActive(true);

        return quotaPlanRepository.save(plan);
    }

    @Override
    public QuotaPlan getQuotaPlanById(Long id) {
        return quotaPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuotaPlan not found: " + id));
    }

    @Override
    public QuotaPlan updateQuotaPlan(Long id, QuotaPlan updated) {
        QuotaPlan existing = getQuotaPlanById(id);

        if (updated.getPlanName() != null && !updated.getPlanName().trim().isEmpty()) {
            existing.setPlanName(updated.getPlanName().trim());
        }
        if (updated.getDailyLimit() > 0) {
            existing.setDailyLimit(updated.getDailyLimit());
        }
        // if active flag is explicitly passed, update it
        existing.setActive(updated.isActive());

        return quotaPlanRepository.save(existing);
    }

    @Override
    public void deactivateQuotaPlan(Long id) {
        QuotaPlan existing = getQuotaPlanById(id);
        existing.setActive(false);
        quotaPlanRepository.save(existing);
    }

    @Override
    public List<QuotaPlan> getAllPlans() {
        return quotaPlanRepository.findAll();
    }
}