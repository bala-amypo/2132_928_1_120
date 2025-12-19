package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.Instant;

@Entity
@Table(name = "api_usage_log")
public class ApiUsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "apiKey is required")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "api_key_id", nullable = false)
    private ApiKey apiKey;

    @NotBlank(message = "endpoint is required")
    @Column(nullable = false)
    private String endpoint;

    @NotNull(message = "timestamp is required")
    @PastOrPresent(message = "timestamp cannot be in the future")
    @Column(nullable = false)
    private Instant timestamp;

    public Long getId() { return id; }
    public ApiKey getApiKey() { return apiKey; }
    public String getEndpoint() { return endpoint; }
    public Instant getTimestamp() { return timestamp; }

    public void setId(Long id) { this.id = id; }
    public void setApiKey(ApiKey apiKey) { this.apiKey = apiKey; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}