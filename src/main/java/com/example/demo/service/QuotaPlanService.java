package com.example.demo.service;

import com.example.demo.dto.QuotaPlanRequestDto;
import com.example.demo.entity.QuotaPlan;

import java.util.List;

public interface QuotaPlanService {

    QuotaPlan create(QuotaPlanRequestDto dto);

    QuotaPlan getById(Long id);

    QuotaPlan getByName(String name);

    List<QuotaPlan> getAll();

    List<QuotaPlan> getActivePlans();

    QuotaPlan update(Long id, QuotaPlanRequestDto dto);

    QuotaPlan deactivate(Long id);

    void delete(Long id);
}