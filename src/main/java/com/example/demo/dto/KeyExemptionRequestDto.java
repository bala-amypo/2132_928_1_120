package com.example.demo.dto;

import jakarta.validation.constraints.*;
import java.time.Instant;

public class KeyExemptionRequestDto {

    @NotNull(message = "apiKeyId is required")
    @Min(value = 1, message = "apiKeyId must be >= 1")
    private Long apiKeyId;

    private String notes;

    @NotNull(message = "unlimitedAccess is required")
    private Boolean unlimitedAccess;

    @Min(value = 0, message = "temporaryExtensionLimit must be >= 0")
    private Integer temporaryExtensionLimit;

    @NotNull(message = "validUntil is required")
    @Future(message = "validUntil must be in the future")
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