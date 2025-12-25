package com.example.demo.repository;

import com.example.demo.entity.ApiUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ApiUsageLogRepository extends JpaRepository<ApiUsageLog, Long> {

    List<ApiUsageLog> findByApiKey_Id(Long apiKeyId);

    @Query("select l from ApiUsageLog l where l.apiKey.id = :apiKeyId and l.timestamp between :from and :to")
    List<ApiUsageLog> findForKeyBetween(@Param("apiKeyId") Long apiKeyId,
                                        @Param("from") Instant from,
                                        @Param("to") Instant to);

    @Query("select count(l) from ApiUsageLog l where l.apiKey.id = :apiKeyId and l.timestamp between :from and :to")
    int countForKeyBetween(@Param("apiKeyId") Long apiKeyId,
                           @Param("from") Instant from,
                           @Param("to") Instant to);
}