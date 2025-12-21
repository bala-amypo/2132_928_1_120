package com.example.demo.service;

import com.example.demo.dto.QuotaPlanRequestDto;
import com.example.demo.entity.QuotaPlan;

import java.util.List;

public interface QuotaPlanService {

    QuotaPlan create(QuotaPlanRequestDto dto);

    QuotaPlan getById(Long id);

    List<QuotaPlan> getAll();

    QuotaPlan update(Long id, QuotaPlanRequestDto dto);

    QuotaPlan deactivate(Long id);

    void delete(Long id);
}