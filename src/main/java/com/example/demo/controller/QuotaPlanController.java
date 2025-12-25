package com.example.demo.controller;

import com.example.demo.entity.QuotaPlan;
import com.example.demo.service.QuotaPlanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quota-plans")
public class QuotaPlanController {

    private final QuotaPlanService quotaPlanService;

    public QuotaPlanController(QuotaPlanService quotaPlanService) {
        this.quotaPlanService = quotaPlanService;
    }

    @PostMapping
    public ResponseEntity<QuotaPlan> create(@Valid @RequestBody QuotaPlan plan) {
        return ResponseEntity.ok(quotaPlanService.createQuotaPlan(plan));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuotaPlan> getById(@PathVariable Long id) {
        return ResponseEntity.ok(quotaPlanService.getQuotaPlanById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuotaPlan> update(@PathVariable Long id, @Valid @RequestBody QuotaPlan plan) {
        return ResponseEntity.ok(quotaPlanService.updateQuotaPlan(id, plan));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivate(@PathVariable Long id) {
        quotaPlanService.deactivateQuotaPlan(id);
        return ResponseEntity.ok("Quota plan deactivated");
    }

    @GetMapping
    public ResponseEntity<List<QuotaPlan>> getAll() {
        return ResponseEntity.ok(quotaPlanService.getAllPlans());
    }
}