package com.example.demo.repository;

import com.example.demo.entity.QuotaPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuotaPlanRepository extends JpaRepository<QuotaPlan, Long> {
    Optional<QuotaPlan> findByPlanName(String planName);
}