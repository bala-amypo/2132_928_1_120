package com.example.demo.service;

import com.example.demo.dto.QuotaPlanRequestDto;
import com.example.demo.entity.QuotaPlan;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.QuotaPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class QuotaPlanServiceImpl implements QuotaPlanService {

    private final QuotaPlanRepository quotaPlanRepository;

    public QuotaPlanServiceImpl(QuotaPlanRepository quotaPlanRepository) {
        this.quotaPlanRepository = quotaPlanRepository;
    }

    @Override
    public QuotaPlan create(QuotaPlanRequestDto dto) {
        String name = dto.getPlanName().trim();

        if (quotaPlanRepository.existsByPlanNameIgnoreCase(name)) {
            throw new BadRequestException("Plan name already exists: " + name);
        }

        QuotaPlan plan = new QuotaPlan();
        plan.setPlanName(name);
        plan.setDailyLimit(dto.getDailyLimit());
        plan.setDescription(dto.getDescription() == null ? null : dto.getDescription().trim());
        plan.setActive(dto.getActive());

        return quotaPlanRepository.save(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public QuotaPlan getById(Long id) {
        return quotaPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuotaPlan not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuotaPlan> getAll() {
        return quotaPlanRepository.findAll();
    }

    @Override
    public QuotaPlan update(Long id, QuotaPlanRequestDto dto) {
        QuotaPlan existing = getById(id);
        String newName = dto.getPlanName().trim();

        if (!existing.getPlanName().equalsIgnoreCase(newName)
                && quotaPlanRepository.existsByPlanNameIgnoreCase(newName)) {
            throw new BadRequestException("Plan name already exists: " + newName);
        }

        existing.setPlanName(newName);
        existing.setDailyLimit(dto.getDailyLimit());
        existing.setDescription(dto.getDescription() == null ? null : dto.getDescription().trim());
        existing.setActive(dto.getActive());

        return quotaPlanRepository.save(existing);
    }

    @Override
    public QuotaPlan deactivate(Long id) {
        QuotaPlan existing = getById(id);
        existing.setActive(false);
        return quotaPlanRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        QuotaPlan existing = getById(id);
        quotaPlanRepository.delete(existing);
    }
}