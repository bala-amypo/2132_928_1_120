package com.example.demo.service;

import com.example.demo.entity.QuotaPlan;

import java.util.List;

public interface QuotaPlanService {
    QuotaPlan createQuotaPlan(QuotaPlan plan);
    QuotaPlan getQuotaPlanById(Long id);
    QuotaPlan updateQuotaPlan(Long id, QuotaPlan updated);
    void deactivateQuotaPlan(Long id);
    List<QuotaPlan> getAllPlans();
}