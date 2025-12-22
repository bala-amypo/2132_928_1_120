// src/main/java/com/example/demo/entity/ApiKey.java
package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "api_key")
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="key_value", nullable = false, unique = true, length = 255)
    private String keyValue;

    @Column(name="owner_id", nullable = false)
    private Long ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="plan_id", nullable = false)
    private QuotaPlan plan;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name="created_at", nullable = false)
    private Instant createdAt;

    @Column(name="updated_at", nullable = false)
    private Instant updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "apiKey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KeyExemption> exemptions = new ArrayList<>();

    public ApiKey() {}

   
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
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public List<KeyExemption> getExemptions() { return exemptions; }
}