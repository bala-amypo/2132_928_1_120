package com.example.demo.repository;

import com.example.demo.entity.RateLimitEnforcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RateLimitEnforcementRepository extends JpaRepository<RateLimitEnforcement, Long> {
    Optional<RateLimitEnforcement> findByApiKeyId(Long apiKeyId);
}