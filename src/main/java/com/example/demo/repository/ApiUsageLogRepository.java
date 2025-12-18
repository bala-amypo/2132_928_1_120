package com.example.demo.repository;

import com.example.demo.entity.ApiUsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ApiUsageLogRepository extends JpaRepository<ApiUsageLog, Long> {

    List<ApiUsageLog> findByApiKey_Id(Long id);

    @Query("select u from ApiUsageLog u where u.apiKey.id = :keyId and u.timestamp between :start and :end order by u.timestamp asc")
    List<ApiUsageLog> findForKeyBetween(@Param("keyId") Long keyId,
                                        @Param("start") Instant start,
                                        @Param("end") Instant end);

    @Query("select count(u) from ApiUsageLog u where u.apiKey.id = :keyId and u.timestamp between :start and :end")
    long countForKeyBetween(@Param("keyId") Long keyId,
                            @Param("start") Instant start,
                            @Param("end") Instant end);
}