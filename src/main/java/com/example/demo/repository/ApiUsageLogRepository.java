package com.example.demo.repository;

import com.example.demo.entity.ApiUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ApiUsageLogRepository extends JpaRepository<ApiUsageLog, Long> {

    // 🔹 HQL: all logs for a key
    @Query("""
        select l
        from ApiUsageLog l
        where l.apiKey.id = :keyId
        order by l.timestamp desc
    """)
    List<ApiUsageLog> findByApiKeyId(@Param("keyId") Long keyId);

    // 🔹 HQL: logs between time
    @Query("""
        select l
        from ApiUsageLog l
        where l.apiKey.id = :keyId
          and l.timestamp between :start and :end
        order by l.timestamp desc
    """)
    List<ApiUsageLog> findByApiKeyIdBetween(
            @Param("keyId") Long keyId,
            @Param("start") Instant start,
            @Param("end") Instant end
    );

    // 🔹 HQL: count today usage
    @Query("""
        select count(l)
        from ApiUsageLog l
        where l.apiKey.id = :keyId
          and l.timestamp between :start and :end
    """)
    long countToday(
            @Param("keyId") Long keyId,
            @Param("start") Instant start,
            @Param("end") Instant end
    );
}