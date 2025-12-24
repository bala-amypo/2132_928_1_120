package com.example.demo.dto;

import java.time.Instant;

public class ApiKeyResponseDto {
    private Long id;
    private String keyValue;
    private Long ownerId;
    private Long planId;
    private String planName;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    public ApiKeyResponseDto(Long id, String keyValue, Long ownerId,
                             Long planId, String planName, Boolean active,
                             Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.keyValue = keyValue;
        this.ownerId = ownerId;
        this.planId = planId;
        this.planName = planName;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getKeyValue() { return keyValue; }
    public Long getOwnerId() { return ownerId; }
    public Long getPlanId() { return planId; }
    public String getPlanName() { return planName; }
    public Boolean getActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}