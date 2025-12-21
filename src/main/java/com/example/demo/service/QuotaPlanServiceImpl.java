package com.example.demo.service;

import com.example.demo.entity.QuotaPlan;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.QuotaPlanRepository;
import org.springframework.stereotype.Service;

@Service
public class QuotaPlanServiceImpl implements QuotaPlanService {

    private final QuotaPlanRepository repository;

    public QuotaPlanServiceImpl(QuotaPlanRepository repository) {
        this.repository = repository;
    }

    @Override
    public QuotaPlan create(QuotaPlan plan) {
        if (repository.existsByPlanName(plan.getPlanName())) {
            throw new BadRequestException("Quota plan already exists");
        }
        return repository.save(plan);
    }
}