package com.example.demo.dto;

import java.time.Instant;

public class RateLimitEnforcementDto {
    private Long id;
    private Long apiKeyId;
    private boolean blocked;
    private String reason;
    private Instant blockedUntil;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getApiKeyId() { return apiKeyId; }
    public void setApiKeyId(Long apiKeyId) { this.apiKeyId = apiKeyId; }

    public boolean isBlocked() { return blocked; }
    public void setBlocked(boolean blocked) { this.blocked = blocked; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Instant getBlockedUntil() { return blockedUntil; }
    public void setBlockedUntil(Instant blockedUntil) { this.blockedUntil = blockedUntil; }
}