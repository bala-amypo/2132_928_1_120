package com.example.demo.controller;

import com.example.demo.dto.QuotaPlanDto;
import com.example.demo.service.QuotaPlanService;
import jakarta.validation.Valid;
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
    public ResponseEntity<QuotaPlanDto> createQuotaPlan(@Valid @RequestBody QuotaPlanDto dto) {
        QuotaPlanDto created = quotaPlanService.createQuotaPlan(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuotaPlanDto> updateQuotaPlan(@PathVariable Long id, @Valid @RequestBody QuotaPlanDto dto) {
        QuotaPlanDto updated = quotaPlanService.updateQuotaPlan(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuotaPlanDto> getQuotaPlanById(@PathVariable Long id) {
        return ResponseEntity.ok(quotaPlanService.getQuotaPlanById(id));
    }

    @GetMapping
    public ResponseEntity<List<QuotaPlanDto>> getAllPlans() {
        return ResponseEntity.ok(quotaPlanService.getAllPlans());
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        quotaPlanService.deactivateQuotaPlan(id);
        return ResponseEntity.noContent().build();
    }
}