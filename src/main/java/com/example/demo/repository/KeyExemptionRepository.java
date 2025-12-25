package com.example.demo.repository;

import com.example.demo.entity.KeyExemption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeyExemptionRepository extends JpaRepository<KeyExemption, Long> {

    // ✅ Required by your ApiRateLimiterQuotaManager
    List<KeyExemption> findByApiKey_Id(Long apiKeyId);
}