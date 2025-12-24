package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "api_key", uniqueConstraints = @UniqueConstraint(name = "uk_api_key_value", columnNames = "key_value"))
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="key_value", nullable=false, unique=true)
    private String keyValue;

    @Column(name="owner_id", nullable=false)
    private Long ownerId;

    // ✅ Referential integrity: plan_id FK NOT NULL
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="plan_id", nullable=false)
    private QuotaPlan plan;

    @Column(nullable=false)
    private Boolean active = true;

    @Column(name="created_at", nullable=false, updatable=false)
    private Instant createdAt;

    @Column(name="updated_at", nullable=false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
        if (active == null) active = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getKeyValue() { return keyValue; }
    public void setKeyValue(String keyValue) { this.keyValue = keyValue; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public QuotaPlan getPlan() { return plan; }
    public void setPlan(QuotaPlan plan) { this.plan = plan; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}