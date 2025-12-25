package com.example.demo.controller;

import com.example.demo.entity.QuotaPlan;
import com.example.demo.service.QuotaPlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<QuotaPlan> create(@RequestBody QuotaPlan plan) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quotaPlanService.createQuotaPlan(plan));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuotaPlan> update(@PathVariable Long id, @RequestBody QuotaPlan plan) {
        return ResponseEntity.ok(quotaPlanService.updateQuotaPlan(id, plan));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuotaPlan> getById(@PathVariable Long id) {
        return ResponseEntity.ok(quotaPlanService.getQuotaPlanById(id));
    }

    @GetMapping
    public ResponseEntity<List<QuotaPlan>> getAll() {
        return ResponseEntity.ok(quotaPlanService.getAllPlans());
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        quotaPlanService.deactivateQuotaPlan(id);
        return ResponseEntity.noContent().build();
    }
}