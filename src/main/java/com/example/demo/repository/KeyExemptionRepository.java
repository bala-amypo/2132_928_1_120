package com.example.demo.repository;

import com.example.demo.entity.KeyExemption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface KeyExemptionRepository extends JpaRepository<KeyExemption, Long> {
    List<KeyExemption> findByApiKey_Id(Long apiKeyId);
    Optional<KeyExemption> findFirstByApiKey_IdOrderByIdDesc(Long apiKeyId);
}