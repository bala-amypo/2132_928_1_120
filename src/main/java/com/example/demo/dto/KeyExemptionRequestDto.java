package com.example.demo.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class KeyExemptionRequestDto {

    @NotNull(message = "apiKeyId is required")
    private Long apiKeyId;

    private String notes;

    @NotNull(message = "unlimitedAccess is required")
    private Boolean unlimitedAccess;

    @Min(value = 0, message = "temporaryExtensionLimit must be >= 0")
    private Integer temporaryExtensionLimit;

    @Future(message = "validUntil must be a future date/time")
    private Instant validUntil;

    public Long getApiKeyId() { return apiKeyId; }
    public void setApiKeyId(Long apiKeyId) { this.apiKeyId = apiKeyId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Boolean getUnlimitedAccess() { return unlimitedAccess; }
    public void setUnlimitedAccess(Boolean unlimitedAccess) { this.unlimitedAccess = unlimitedAccess; }

    public Integer getTemporaryExtensionLimit() { return temporaryExtensionLimit; }
    public void setTemporaryExtensionLimit(Integer temporaryExtensionLimit) { this.temporaryExtensionLimit = temporaryExtensionLimit; }

    public Instant getValidUntil() { return validUntil; }
    public void setValidUntil(Instant validUntil) { this.validUntil = validUntil; }
}