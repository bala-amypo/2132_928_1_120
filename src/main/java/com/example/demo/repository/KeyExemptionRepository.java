package com.example.demo.repository;

import com.example.demo.entity.KeyExemption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KeyExemptionRepository extends JpaRepository<KeyExemption, Long> {

    Optional<KeyExemption> findByApiKeyId(Long apiKeyId);

    // ✅ test expects this exact method name
    List<KeyExemption> findByApiKey_Id(long apiKeyId);
}