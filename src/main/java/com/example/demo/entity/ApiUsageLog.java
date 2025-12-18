package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "api_usage_log")
public class ApiUsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "api_key_id", nullable = false)
    private ApiKey apiKey;

    @Column(nullable = false)
    private String endpoint;

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