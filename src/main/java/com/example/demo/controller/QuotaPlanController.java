package com.example.demo.controller;

import com.example.demo.entity.QuotaPlan;
import com.example.demo.service.QuotaPlanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quota-plans")
public class QuotaPlanController {

    private final QuotaPlanService quotaPlanService;

    public QuotaPlanController(QuotaPlanService quotaPlanService) {
        this.quotaPlanService = quotaPlanService;
    }

    @PostMapping
    public QuotaPlan create(@RequestBody QuotaPlan plan) {
        return quotaPlanService.createQuotaPlan(plan);
    }

    @PutMapping("/{id}")
    public QuotaPlan update(@PathVariable Long id, @RequestBody QuotaPlan plan) {
        return quotaPlanService.updateQuotaPlan(id, plan);
    }

    @GetMapping("/{id}")
    public QuotaPlan getById(@PathVariable Long id) {
        return quotaPlanService.getQuotaPlanById(id);
    }

    @GetMapping
    public List<QuotaPlan> getAll() {
        return quotaPlanService.getAllPlans();
    }

    @PutMapping("/{id}/deactivate")
    public QuotaPlan deactivate(@PathVariable Long id) {
        return quotaPlanService.deactivateQuotaPlan(id);
    }
}