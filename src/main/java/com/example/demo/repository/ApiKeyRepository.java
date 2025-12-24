package com.example.demo.repository;

import com.example.demo.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    @Query("select k from ApiKey k where k.keyValue = :keyValue")
    Optional<ApiKey> findByKeyValue(@Param("keyValue") String keyValue);

    @Query("select count(k) > 0 from ApiKey k where k.keyValue = :keyValue")
    boolean existsByKeyValue(@Param("keyValue") String keyValue);
}