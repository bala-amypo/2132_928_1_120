package com.example.demo.repository;

import com.example.demo.entity.ApiUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ApiUsageLogRepository extends JpaRepository<ApiUsageLog, Long> {

    // ✅ test expects this exact method name
    List<ApiUsageLog> findByApiKey_Id(long apiKeyId);

    // (optional, keep if you already use)
    List<ApiUsageLog> findByApiKeyId(Long apiKeyId);

    @Query("select a from ApiUsageLog a where a.apiKey.id = :keyId and a.timestamp >= :start and a.timestamp < :end")
    List<ApiUsageLog> findForKeyBetween(@Param("keyId") Long keyId,
                                        @Param("start") Instant start,
                                        @Param("end") Instant end);

    @Query("select count(a) from ApiUsageLog a where a.apiKey.id = :keyId and a.timestamp >= :start and a.timestamp < :end")
    int countForKeyBetween(@Param("keyId") Long keyId,
                           @Param("start") Instant start,
                           @Param("end") Instant end);
}