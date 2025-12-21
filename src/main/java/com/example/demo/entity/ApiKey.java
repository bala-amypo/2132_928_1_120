package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "api_key")
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="key_value", nullable = false, length = 150, unique = true)
    private String keyValue;

    @Column(name="owner_id", nullable = false)
    private Long ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private QuotaPlan plan;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name="updated_at", nullable = false)
    private Instant updatedAt;

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
    public String getKeyValue() { return keyValue; }
    public Long getOwnerId() { return ownerId; }
    public QuotaPlan getPlan() { return plan; }
    public Boolean getActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setKeyValue(String keyValue) { this.keyValue = keyValue; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public void setPlan(QuotaPlan plan) { this.plan = plan; }
    public void setActive(Boolean active) { this.active = active; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}