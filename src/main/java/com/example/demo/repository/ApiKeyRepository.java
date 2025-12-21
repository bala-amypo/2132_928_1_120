package com.example.demo.repository;

import com.example.demo.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    long countByPlan_Id(Long planId);
}