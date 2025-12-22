package com.example.demo.dto;

import java.time.Instant;

public class KeyExemptionResponseDto {
    private Long id;
    private Long apiKeyId;
    private String notes;
    private Boolean unlimitedAccess;
    private Integer temporaryExtensionLimit;
    private Instant validUntil;
    private Instant createdAt;
    private Instant updatedAt;

    public KeyExemptionResponseDto() {}

    public KeyExemptionResponseDto(Long id, Long apiKeyId, String notes, Boolean unlimitedAccess,
                                  Integer temporaryExtensionLimit, Instant validUntil,
                                  Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.apiKeyId = apiKeyId;
        this.notes = notes;
        this.unlimitedAccess = unlimitedAccess;
        this.temporaryExtensionLimit = temporaryExtensionLimit;
        this.validUntil = validUntil;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public Long getApiKeyId() { return apiKeyId; }
    public String getNotes() { return notes; }
    public Boolean getUnlimitedAccess() { return unlimitedAccess; }
    public Integer getTemporaryExtensionLimit() { return temporaryExtensionLimit; }
    public Instant getValidUntil() { return validUntil; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}