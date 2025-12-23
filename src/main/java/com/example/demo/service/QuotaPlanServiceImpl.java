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
public class QuotaPlanServiceImpl implements QuotaPlanService {

    private final QuotaPlanRepository quotaPlanRepository;

    public QuotaPlanServiceImpl(QuotaPlanRepository quotaPlanRepository) {
        this.quotaPlanRepository = quotaPlanRepository;
    }

    @Override
    @Transactional
    public QuotaPlan create(QuotaPlanRequestDto dto) {

        String name = dto.getPlanName().trim();

        if (quotaPlanRepository.existsByPlanNameIgnoreCase(name)) {
            throw new BadRequestException("Plan name already exists: " + name);
        }

        QuotaPlan plan = new QuotaPlan();
        plan.setPlanName(name);
        plan.setDailyLimit(dto.getDailyLimit());
        plan.setDescription(trim(dto.getDescription()));
        plan.setActive(dto.getActive());

        // ✅ SAVE FIRST
        QuotaPlan saved = quotaPlanRepository.save(plan);

        // 🔴 rollback demo
        if ("AIML".equalsIgnoreCase(saved.getPlanName())) {
            throw new ResourceNotFoundException("Testing Transaction Rollback");
        }

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public QuotaPlan getById(Long id) {
        return quotaPlanRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("QuotaPlan not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public QuotaPlan getByName(String name) {
        return quotaPlanRepository.findByPlanNameHql(name)
                .orElseThrow(() ->
                        new ResourceNotFoundException("QuotaPlan not found with name: " + name));
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuotaPlan> getAll() {
        return quotaPlanRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuotaPlan> getActivePlans() {
        return quotaPlanRepository.findActivePlansHql();
    }

    @Override
    @Transactional
    public QuotaPlan update(Long id, QuotaPlanRequestDto dto) {

        QuotaPlan existing = getById(id);
        String newName = dto.getPlanName().trim();

        if (!existing.getPlanName().equalsIgnoreCase(newName)
                && quotaPlanRepository.existsByPlanNameIgnoreCase(newName)) {
            throw new BadRequestException("Plan name already exists: " + newName);
        }

        existing.setPlanName(newName);
        existing.setDailyLimit(dto.getDailyLimit());
        existing.setDescription(trim(dto.getDescription()));
        existing.setActive(dto.getActive());

        QuotaPlan saved = quotaPlanRepository.save(existing);

        if ("AIML".equalsIgnoreCase(saved.getPlanName())) {
            throw new ResourceNotFoundException("Testing");
        }

        return saved;
    }

    @Override
    @Transactional
    public QuotaPlan deactivate(Long id) {

        QuotaPlan plan = getById(id);
        plan.setActive(false);

        QuotaPlan saved = quotaPlanRepository.save(plan);

        if ("AIML".equalsIgnoreCase(saved.getPlanName())) {
            throw new ResourceNotFoundException("Testing");
        }

        return saved;
    }

    @Override
    @Transactional
    public void delete(Long id) {

        QuotaPlan plan = getById(id);
        quotaPlanRepository.delete(plan);

        if ("AIML".equalsIgnoreCase(plan.getPlanName())) {
            throw new ResourceNotFoundException("Testing");
        }
    }

    private String trim(String s) {
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }
}