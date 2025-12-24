package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ApiUsageLogRequestDto {
    @NotNull private Long apiKeyId;
    @NotBlank private String endpoint;

    public Long getApiKeyId() { return apiKeyId; }
    public String getEndpoint() { return endpoint; }
    public void setApiKeyId(Long apiKeyId) { this.apiKeyId = apiKeyId; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
}