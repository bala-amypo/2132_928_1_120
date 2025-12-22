package com.example.demo.controller;

import com.example.demo.dto.QuotaPlanRequestDto;
import com.example.demo.entity.QuotaPlan;
import com.example.demo.service.QuotaPlanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public QuotaPlan create(@Valid @RequestBody QuotaPlanRequestDto dto) {
        return quotaPlanService.create(dto);
    }

  
    @GetMapping("/{id}")
    public QuotaPlan getById(@PathVariable Long id) {
        return quotaPlanService.getById(id);
    }

    @GetMapping
    public List<QuotaPlan> getAll() {
        return quotaPlanService.getAll();
    }

    @PutMapping("/{id}")
    public QuotaPlan update(@PathVariable Long id, @Valid @RequestBody QuotaPlanRequestDto dto) {
        return quotaPlanService.update(id, dto);
    }

    @PutMapping("/{id}/deactivate")
    public QuotaPlan deactivate(@PathVariable Long id) {
        return quotaPlanService.deactivate(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        quotaPlanService.delete(id);
    }
}