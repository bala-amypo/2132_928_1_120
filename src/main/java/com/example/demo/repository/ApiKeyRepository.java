package com.example.demo.repository;

import com.example.demo.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    // ✅ HQL: find by keyValue
    @Query("select k from ApiKey k where k.keyValue = :keyValue")
    Optional<ApiKey> findByKeyValueHql(@Param("keyValue") String keyValue);

    // ✅ HQL: check existence
    @Query("select count(k) > 0 from ApiKey k where k.keyValue = :keyValue")
    boolean existsByKeyValueHql(@Param("keyValue") String keyValue);

    // ✅ HQL: get only ACTIVE keys
    @Query("select k from ApiKey k where k.active = true")
    List<ApiKey> findAllActiveKeysHql();
}