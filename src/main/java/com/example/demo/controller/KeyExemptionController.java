package com.example.demo.controller;

import com.example.demo.entity.KeyExemption;
import com.example.demo.service.KeyExemptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/key-exemptions")
public class KeyExemptionController {

    private final KeyExemptionService service;

    public KeyExemptionController(KeyExemptionService service) {
        this.service = service;
    }

    @PostMapping
    public KeyExemption create(@RequestBody KeyExemption exemption) {
        return service.createExemption(exemption);
    }

    @PutMapping("/{id}")
    public KeyExemption update(@PathVariable Long id, @RequestBody KeyExemption exemption) {
        return service.updateExemption(id, exemption);
    }

    @GetMapping("/key/{keyId}")
    public KeyExemption getByKey(@PathVariable Long keyId) {
        return service.getExemptionByKey(keyId);
    }

    @GetMapping
    public List<KeyExemption> getAll() {
        return service.getAllExemptions();
    }
}