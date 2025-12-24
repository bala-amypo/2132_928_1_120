package com.example.demo.dto;

import java.time.Instant;

public class ApiKeyDto {
    private Long id;
    private String keyValue;
    private Long ownerId;
    private Long planId;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    public ApiKeyDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getKeyValue() { return keyValue; }
    public void setKeyValue(String keyValue) { this.keyValue = keyValue; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}