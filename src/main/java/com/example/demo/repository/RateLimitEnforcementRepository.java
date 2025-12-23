package com.example.demo.repository;

import com.example.demo.entity.RateLimitEnforcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface RateLimitEnforcementRepository extends JpaRepository<RateLimitEnforcement, Long> {

    @Query("""
        select r
        from RateLimitEnforcement r
        where r.apiKey.id = :apiKeyId
        order by r.blockedAt desc
    """)
    List<RateLimitEnforcement> findByApiKey_IdOrderByBlockedAtDesc(@Param("apiKeyId") Long apiKeyId);

    @Query("""
        select r
        from RateLimitEnforcement r
        where r.apiKey.id = :apiKeyId
          and r.blockedAt between :start and :end
        order by r.blockedAt desc
    """)
    List<RateLimitEnforcement> findByApiKey_IdAndBlockedAtBetweenOrderByBlockedAtDesc(
            @Param("apiKeyId") Long apiKeyId,
            @Param("start") Instant start,
            @Param("end") Instant end
    );
}