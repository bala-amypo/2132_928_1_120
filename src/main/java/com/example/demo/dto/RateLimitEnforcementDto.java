package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class RateLimitEnforcementDto {
    private Long id;

    @NotNull(message = "API Key ID is required")
    private Long apiKeyId;

    @NotNull(message = "Blocked at timestamp is required")
    private Instant blockedAt;

    @NotNull(message = "Limit exceeded by is required")
    @Min(value = 1, message = "Limit exceeded must be at least 1")
    private Integer limitExceededBy;

    private String message;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getApiKeyId() { return apiKeyId; }
    public void setApiKeyId(Long apiKeyId) { this.apiKeyId = apiKeyId; }

    public Instant getBlockedAt() { return blockedAt; }
    public void setBlockedAt(Instant blockedAt) { this.blockedAt = blockedAt; }

    public Integer getLimitExceededBy() { return limitExceededBy; }
    public void setLimitExceededBy(Integer limitExceededBy) { this.limitExceededBy = limitExceededBy; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}