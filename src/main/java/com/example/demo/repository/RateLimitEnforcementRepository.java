package com.example.demo.repository;

import com.example.demo.entity.RateLimitEnforcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RateLimitEnforcementRepository extends JpaRepository<RateLimitEnforcement, Long> {

    @Query("select e from RateLimitEnforcement e where e.apiKey.id = :keyId order by e.blockedAt desc")
    List<RateLimitEnforcement> findByApiKeyId(@Param("keyId") Long keyId);
}