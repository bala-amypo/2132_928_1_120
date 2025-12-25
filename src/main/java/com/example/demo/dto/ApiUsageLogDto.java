package com.example.demo.dto;

import java.time.Instant;

public class ApiUsageLogDto {
    private Long id;
    private Long apiKeyId;
    private Instant requestTimestamp;
    private String endpoint;
    private String httpMethod;
    private boolean success;
    private Integer responseCode;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getApiKeyId() { return apiKeyId; }
    public void setApiKeyId(Long apiKeyId) { this.apiKeyId = apiKeyId; }

    public Instant getRequestTimestamp() { return requestTimestamp; }
    public void setRequestTimestamp(Instant requestTimestamp) { this.requestTimestamp = requestTimestamp; }

    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public Integer getResponseCode() { return responseCode; }
    public void setResponseCode(Integer responseCode) { this.responseCode = responseCode; }
}