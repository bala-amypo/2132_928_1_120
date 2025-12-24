package com.example.demo.repository;

import com.example.demo.entity.QuotaPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuotaPlanRepository extends JpaRepository<QuotaPlan, Long> {

    // HQL by name
    @Query("select q from QuotaPlan q where lower(q.planName) = lower(:name)")
    Optional<QuotaPlan> findByPlanName(@Param("name") String name);

    // HQL active plans
    @Query("select q from QuotaPlan q where q.active = :active")
    List<QuotaPlan> findByActive(@Param("active") boolean active);
}