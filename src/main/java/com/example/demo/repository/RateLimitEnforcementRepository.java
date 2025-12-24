package com.example.demo.repository;

import com.example.demo.entity.RateLimitEnforcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface RateLimitEnforcementRepository extends JpaRepository<RateLimitEnforcement, Long> {

    @Query("""
        select r from RateLimitEnforcement r
        where r.apiKey.id = :keyId
        order by r.blockedAt desc
    """)
    List<RateLimitEnforcement> findByApiKeyId(@Param("keyId") Long keyId);

    @Query("""
        select r from RateLimitEnforcement r
        where r.apiKey.id = :keyId
        and r.blockedAt between :start and :end
        order by r.blockedAt desc
    """)
    List<RateLimitEnforcement> findForKeyBetween(@Param("keyId") Long keyId,
                                                 @Param("start") Instant start,
                                                 @Param("end") Instant end);
}