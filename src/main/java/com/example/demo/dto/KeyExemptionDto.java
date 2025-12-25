package com.example.demo.dto;

public class KeyExemptionDto {
    private Long id;
    private Long apiKeyId;
    private boolean exempted;
    private String reason;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getApiKeyId() { return apiKeyId; }
    public void setApiKeyId(Long apiKeyId) { this.apiKeyId = apiKeyId; }

    public boolean isExempted() { return exempted; }
    public void setExempted(boolean exempted) { this.exempted = exempted; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}