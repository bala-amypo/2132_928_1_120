package com.example.demo.controller;

import com.example.demo.entity.QuotaPlan;
import com.example.demo.service.QuotaPlanService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/quota-plans")
public class QuotaPlanController {

    private final QuotaPlanService quotaPlanService;

    public QuotaPlanController(QuotaPlanService quotaPlanService) {
        this.quotaPlanService = quotaPlanService;
    }

    @PostMapping
    public QuotaPlan create(@Valid @RequestBody QuotaPlan plan) {
        return quotaPlanService.createQuotaPlan(plan);
    }

    @PutMapping("/{id}")
    public QuotaPlan update(@PathVariable @Min(1) Long id, @Valid @RequestBody QuotaPlan plan) {
        return quotaPlanService.updateQuotaPlan(id, plan);
    }

    @GetMapping("/{id}")
    public QuotaPlan getById(@Path