package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class RateLimitEnforcementRequestDto {
    @NotNull private Long apiKeyId;
    private Instant blockedAt;
    @NotNull @Min(1) private Integer limitExceededBy;
    @NotBlank private String message;

    public Long getApiKeyId() { return apiKeyId; }
    public Instant getBlockedAt() { return blockedAt; }
    public Integer getLimitExceededBy() { return limitExceededBy; }
    public String getMessage() { return message; }

    public void setApiKeyId(Long apiKeyId) { this.apiKeyId = apiKeyId; }
    public void setBlockedAt(Instant blockedAt) { this.blockedAt = blockedAt; }
    public void setLimitExceededBy(Integer limitExceededBy) { this.limitExceededBy = limitExceededBy; }
    public void setMessage(String message) { this.message = message; }
}