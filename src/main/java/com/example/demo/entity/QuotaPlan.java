package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "quota_plan",
        uniqueConstraints = @UniqueConstraint(name = "uk_quota_plan_plan_name", columnNames = "plan_name")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuotaPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="plan_name", nullable = false, length = 100)
    private String planName;

    @Column(name="daily_limit", nullable = false)
    private Long dailyLimit;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name="updated_at", nullable = false)
    private Instant updatedAt;

    // Relationship: One plan -> many ApiKeys
    @OneToMany(mappedBy = "plan", fetch = FetchType.LAZY)
    private List<ApiKey> apiKeys = new ArrayList<>();

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
        if (active == null) active = true;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
        if (active == null) active = true;
    }
}