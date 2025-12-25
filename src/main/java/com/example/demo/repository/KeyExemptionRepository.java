package com.example.demo.repository;

import com.example.demo.entity.KeyExemption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeyExemptionRepository extends JpaRepository<KeyExemption, Long> {

    // ✅ tests expect this exact method AND Optional return type (your error shows Optional in thenReturn)
    Optional<KeyExemption> findByApiKey_Id(long apiKeyId);

    // keep your existing one too (service uses it)
    Optional<KeyExemption> findByApiKeyId(Long apiKeyId);
}