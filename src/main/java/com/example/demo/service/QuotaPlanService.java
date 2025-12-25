package com.example.demo.service;

import com.example.demo.entity.QuotaPlan;
import java.util.List;

public interface QuotaPlanService {
    QuotaPlan createQuotaPlan(QuotaPlan plan);
    QuotaPlan updateQuotaPlan(Long id, QuotaPlan plan);
    QuotaPlan getQuotaPlanById(Long id);
    List<QuotaPlan> getAllPlans();
    void deactivateQuotaPlan(Long id);
}