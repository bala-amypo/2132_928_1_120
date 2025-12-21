package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "quota_plan",
        uniqueConstraints = @UniqueConstraint(name = "uk_quota_plan_plan_name", columnNames = "plan_name")
)
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

    public Long getId() { return id; }
    public String getPlanName() { return planName; }
    public Long getDailyLimit() { return dailyLimit; }
    public String getDescription() { return description; }
    public Boolean getActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public List<ApiKey> getApiKeys() { return apiKeys; }

    public void setId(Long id) { this.id = id; }
    public void setPlanName(String planName) { this.planName = planName; }
    public void setDailyLimit(Long dailyLimit) { this.dailyLimit = dailyLimit; }
    public void setDescription(String description) { this.description = description; }
    public void setActive(Boolean active) { this.active = active; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public void setApiKeys(List<ApiKey> apiKeys) { this.apiKeys = apiKeys; }
}