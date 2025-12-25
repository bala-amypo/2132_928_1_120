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

        if (plan.getDailyLimit() == null || plan.getDailyLimit() <= 0) {
            throw new BadRequestException("Daily limit must be > 0");
        }

        quotaPlanRepository.findByPlanName(plan.getPlanName())
                .ifPresent(x -> { throw new BadRequestException("Plan name already exists"); });

        if (plan.getActive() == null) plan.setActive(true);

        return quotaPlanRepository.save(plan);
    }

    @Override
    public QuotaPlan updateQuotaPlan(Long id, QuotaPlan incoming) {

        QuotaPlan plan = quotaPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quota plan not found"));

        if (incoming.getDailyLimit() == null || incoming.getDailyLimit() <= 0) {
            throw new BadRequestException("Daily limit must be > 0");
        }

        if (incoming.getPlanName() != null && !incoming.getPlanName().equalsIgnoreCase(plan.getPlanName())) {
            quotaPlanRepository.findByPlanName(incoming.getPlanName())
                    .ifPresent(x -> { throw new BadRequestException("Plan name already exists"); });
            plan.setPlanName(incoming.getPlanName());
        }

        plan.setDailyLimit(incoming.getDailyLimit());
        plan.setDescription(incoming.getDescription());

        if (incoming.getActive() != null) {
            plan.setActive(incoming.getActive());
        }

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