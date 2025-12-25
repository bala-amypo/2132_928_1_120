package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class ApiUsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private ApiKey apiKey;

    private String endpoint;

    // test uses setTimestamp/getTimestamp
    private Instant timestamp;

    // (extra aliases to avoid earlier compile errors in your project)
    private String method;
    private boolean success;
    private int responseCode;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ApiKey getApiKey() { return apiKey; }
    public void setApiKey(ApiKey apiKey) { this.apiKey = apiKey; }

    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    // aliases (some of your earlier code expected these)
    public Instant getRequestTimestamp() { return timestamp; }
    public void setRequestTimestamp(Instant ts) { this.timestamp = ts; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public int getResponseCode() { return responseCode; }
    public void setResponseCode(int responseCode) { this.responseCode = responseCode; }
}