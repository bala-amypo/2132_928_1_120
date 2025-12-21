package com.example.demo.repository;

import com.example.demo.entity.ApiUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface ApiUsageLogRepository extends JpaRepository<ApiUsageLog, Long> {

    List<ApiUsageLog> findByApiKey_IdOrderByTimestampDesc(Long keyId);

    List<ApiUsageLog> findByApiKey_IdAndTimestampBetweenOrderByTimestampDesc(
            Long keyId, Instant start, Instant end
    );

    long countByApiKey_IdAndTimestampBetween(Long keyId, Instant start, Instant end);
}