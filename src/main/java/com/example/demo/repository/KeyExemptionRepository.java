package com.example.demo.repository;

import com.example.demo.entity.KeyExemption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KeyExemptionRepository extends JpaRepository<KeyExemption, Long> {

    // 🔹 HQL: exemptions by API key
    @Query("select k from KeyExemption k where k.apiKey.id = :apiKeyId")
    List<KeyExemption> findByApiKeyId(@Param("apiKeyId") Long apiKeyId);

    // 🔹 HQL: latest exemption
    @Query("""
        select k
        from KeyExemption k
        where k.apiKey.id = :apiKeyId
        order by k.id desc
    """)
    Optional<KeyExemption> findLatestByApiKeyId(@Param("apiKeyId") Long apiKeyId);
}