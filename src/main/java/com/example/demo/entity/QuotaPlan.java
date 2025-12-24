package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "quota_plan",
        uniqueConstraints = @UniqueConstraint(name = "uk_quota_plan_name", columnNames = "plan_name")
)
public class QuotaPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="plan_name", nullable=false, length=255)
    private String planName;

    @Column(name="daily_limit", nullable=false)
    private Integer dailyLimit;

    @Column(name="description", length=255)
    private String description;

    @Column(name="active", nullable=false)
    private Boolean active = true;

    @Column(name="created_at", nullable=false, updatable=false)
    private Instant createdAt;

    @Column(name="updated_at", nullable=false)
    private Instant updatedAt;

    // ✅ Inverse mapping (no cascade delete)
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
    }

    public Long getId() { return id; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public Integer getDailyLimit() { return dailyLimit; }
    public void setDailyLimit(Integer dailyLimit) { this.dailyLimit = dailyLimit; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public List<ApiKey> getApiKeys() { return apiKeys; }
}