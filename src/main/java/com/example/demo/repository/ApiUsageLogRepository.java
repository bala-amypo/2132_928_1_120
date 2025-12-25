package com.example.demo.repository;

import com.example.demo.entity.ApiUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ApiUsageLogRepository extends JpaRepository<ApiUsageLog, Long> {

    List<ApiUsageLog> findByApiKeyId(Long apiKeyId);
    List<com.example.demo.entity.ApiUsageLog> findByApiKey_Id(long apiKeyId);

    @Query("select l from ApiUsageLog l where l.apiKey.id = :apiKeyId and l.requestTimestamp between :from and :to")
    List<ApiUsageLog> findForKeyBetween(@Param("apiKeyId") Long apiKeyId,
                                        @Param("from") Instant from,
                                        @Param("to") Instant to);

    @Query("select count(l) from ApiUsageLog l where l.apiKey.id = :apiKeyId and l.requestTimestamp between :from and :to")
    long countForKeyBetween(@Param("apiKeyId") Long apiKeyId,
                            @Param("from") Instant from,
                            @Param("to") Instant to);
    java.util.Optional<com.example.demo.entity.KeyExemption> findByApiKey_Id(long apiKeyId);                        
}