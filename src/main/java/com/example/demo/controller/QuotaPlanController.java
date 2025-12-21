package com.example.demo.controller;

import com.example.demo.entity.QuotaPlan;
import com.example.demo.service.QuotaPlanService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quota-plans")
public class QuotaPlanController {

    private final QuotaPlanService service;

    public QuotaPlanController(QuotaPlanService service) {
        this.service = service;
    }

    @PostMapping
    public QuotaPlan create(@RequestBody @Valid QuotaPlan plan) {
        return service.create(plan);
    }
}