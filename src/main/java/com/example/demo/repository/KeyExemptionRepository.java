package com.example.demo.repository;

import com.example.demo.entity.KeyExemption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KeyExemptionRepository extends JpaRepository<KeyExemption, Long> {

    // 🔹 Get all exemptions for an API key (HQL)
    @Query("""
        select k
        from KeyExemption k
        where k.apiKey.id = :apiKeyId
        order by k.id desc
    """)
    List<KeyExemption> findByApiKeyHql(@Param("apiKeyId") Long apiKeyId);

    // 🔹 Get latest exemption for an API key (HQL)
    @Query("""
        select k
        from KeyExemption k
        where k.apiKey.id = :apiKeyId
        order by k.id desc
    """)
    Optional<KeyExemption> findLatestByApiKeyHql(@Param("apiKeyId") Long apiKeyId);
}