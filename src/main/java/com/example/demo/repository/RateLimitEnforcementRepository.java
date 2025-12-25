package com.example.demo.repository;

import com.example.demo.entity.RateLimitEnforcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RateLimitEnforcementRepository extends JpaRepository<RateLimitEnforcement, Long> {

    // ✅ tests expect this exact derived name
    List<RateLimitEnforcement> findByApiKey_Id(long apiKeyId);

    // keep your existing one too (service uses it)
    List<RateLimitEnforcement> findByApiKeyId(Long apiKeyId);
}