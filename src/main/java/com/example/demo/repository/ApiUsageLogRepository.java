package com.example.demo.repository;

import com.example.demo.entity.ApiUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ApiUsageLogRepository extends JpaRepository<ApiUsageLog, Long> {

    // 🔹 HQL: Get logs by key
    @Query("""
        select l
        from ApiUsageLog l
        where l.apiKey.id = :keyId
        order by l.timestamp desc
    """)
    List<ApiUsageLog> findByApiKeyHql(@Param("keyId") Long keyId);

    // 🔹 HQL: Get logs between dates
    @Query("""
        select l
        from ApiUsageLog l
        where l.apiKey.id = :keyId
          and l.timestamp between :start and :end
        order by l.timestamp desc
    """)
    List<ApiUsageLog> findUsageBetweenHql(
            @Param("keyId") Long keyId,
            @Param("start") Instant start,
            @Param("end") Instant end
    );

    // 🔹 HQL: Count logs
    @Query("""
        select count(l)
        from ApiUsageLog l
        where l.apiKey.id = :keyId
          and l.timestamp between :start and :end
    """)
    long countUsageHql(
            @Param("keyId") Long keyId,
            @Param("start") Instant start,
            @Param("end") Instant end
    );
}