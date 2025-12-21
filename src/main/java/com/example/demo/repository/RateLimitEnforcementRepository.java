package com.example.demo.repository;

import com.example.demo.entity.RateLimitEnforcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface RateLimitEnforcementRepository extends JpaRepository<RateLimitEnforcement, Long> {

    List<RateLimitEnforcement> findByApiKey_IdOrderByBlockedAtDesc(Long apiKeyId);

    List<RateLimitEnforcement> findByApiKey_IdAndBlockedAtBetweenOrderByBlockedAtDesc(
            Long apiKeyId, Instant start, Instant end
    );
}