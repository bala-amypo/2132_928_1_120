package com.example.demo.repository;

import com.example.demo.entity.RateLimitEnforcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface RateLimitEnforcementRepository
        extends JpaRepository<RateLimitEnforcement, Long> {

    // 🔹 HQL: get by API key
    @Query("""
        select r
        from RateLimitEnforcement r
        where r.apiKey.id = :apiKeyId
        order by r.blockedAt desc
    """)
    List<RateLimitEnforcement> findByApiKeyHql(
            @Param("apiKeyId") Long apiKeyId
    );

    // 🔹 HQL: get by API key between dates
    @Query("""
        select r
        from RateLimitEnforcement r
        where r.apiKey.id = :apiKeyId
          and r.blockedAt between :from and :to
        order by r.blockedAt desc
    """)
    List<RateLimitEnforcement> findByApiKeyBetweenHql(
            @Param("apiKeyId") Long apiKeyId,
            @Param("from") Instant from,
            @Param("to") Instant to
    );
}