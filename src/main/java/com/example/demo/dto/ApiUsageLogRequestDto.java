package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.Instant;

public class ApiUsageLogRequestDto {

    @NotNull(message = "apiKeyId is required")
    @Positive(message = "apiKeyId must be > 0")
    private Long apiKeyId;

    @NotBlank(message = "endpoint is required")
    private String endpoint;

    @NotNull(message = "timestamp is required")
    @PastOrPresent(message = "timestamp cannot be in the future")
    private Instant timestamp;

    public Long getApiKeyId() { return apiKeyId; }
    public String getEndpoint() { return endpoint; }
    public Instant getTimestamp() { return timestamp; }

    public void setApiKeyId(Long apiKeyId) { this.apiKeyId = apiKeyId; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}