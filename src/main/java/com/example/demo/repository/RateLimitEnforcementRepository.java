package com.example.demo.repository;

import com.example.demo.entity.RateLimitEnforcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RateLimitEnforcementRepository extends JpaRepository<RateLimitEnforcement, Long> {

    // ✅ test expects this exact method name
    List<RateLimitEnforcement> findByApiKey_Id(long apiKeyId);

    // (optional, keep if you already use)
    List<RateLimitEnforcement> findByApiKeyId(Long apiKeyId);
}