package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Instant;

@Entity
@Table(name = "api_key", uniqueConstraints = @UniqueConstraint(columnNames = "keyValue"))
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "keyValue is required")
    @Column(nullable = false)
    private String keyValue;

    @NotNull(message = "ownerId is required")
    @Positive(message = "ownerId must be > 0")
    @Column(nullable = false)
    private Long ownerId;

    @NotNull(message = "plan is required")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "plan_id", nullable = false)
    private QuotaPlan plan;

    @NotNull(message = "active is required")
    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
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