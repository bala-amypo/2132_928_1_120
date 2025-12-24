package com.example.demo.repository;

import com.example.demo.entity.ApiUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ApiUsageLogRepository extends JpaRepository<ApiUsageLog, Long> {

    @Query("""
        select l from ApiUsageLog l
        where l.apiKey.id = :keyId
        order by l.timestamp desc
    """)
    List<ApiUsageLog> findByApiKey_IdOrderByTimestampDesc(@Param("keyId") Long keyId);

    @Query("""
        select l from ApiUsageLog l
        where l.apiKey.id = :keyId
        and l.timestamp between :start and :end
        order by l.timestamp desc
    """)
    List<ApiUsageLog> findForKeyBetween(@Param("keyId") Long keyId,
                                        @Param("start") Instant start,
                                        @Param("end") Instant end);

    @Query("""
        select count(l) from ApiUsageLog l
        where l.apiKey.id = :keyId
        and l.timestamp between :start and :end
    """)
    long countForKeyBetween(@Param("keyId") Long keyId,
                            @Param("start") Instant start,
                            @Param("end") Instant end);
}