package com.example.demo.repository;

import com.example.demo.entity.KeyExemption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface KeyExemptionRepository
        extends JpaRepository<KeyExemption, Long> {

    /* REQUIRED BY TESTS */
    Optional<KeyExemption> findByApiKey_Id(Long apiKeyId);

    /* YOUR HQL (KEEP) */
    @Query("""
        select e from KeyExemption e
        where e.apiKey.id = :keyId
    """)
    Optional<KeyExemption> findByApiKeyId(@Param("keyId") Long keyId);
}