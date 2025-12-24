package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class KeyExemptionRequestDto {
    @NotNull private Long apiKeyId;
    private String notes;
    private Boolean unlimitedAccess;
    private Integer temporaryExtensionLimit;
    private Instant validUntil;

    public Long getApiKeyId() { return apiKeyId; }
    public String getNotes() { return notes; }
    public Boolean getUnlimitedAccess() { return unlimitedAccess; }
    public Integer getTemporaryExtensionLimit() { return temporaryExtensionLimit; }
    public Instant getValidUntil() { return validUntil; }

    public void setApiKeyId(Long apiKeyId) { this.apiKeyId = apiKeyId; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setUnlimitedAccess(Boolean unlimitedAccess) { this.unlimitedAccess = unlimitedAccess; }
    public void setTemporaryExtensionLimit(Integer temporaryExtensionLimit) { this.temporaryExtensionLimit = temporaryExtensionLimit; }
    public void setValidUntil(Instant validUntil) { this.validUntil = validUntil; }
}