package com.example.demo.repository;

import com.example.demo.entity.QuotaPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuotaPlanRepository extends JpaRepository<QuotaPlan, Long> {

    @Query("""
        select count(q) > 0
        from QuotaPlan q
        where lower(q.planName) = lower(:planName)
    """)
    boolean existsByPlanNameIgnoreCase(@Param("planName") String planName);

    @Query("select q from QuotaPlan q where q.active = true")
    List<QuotaPlan> findActivePlans();
}