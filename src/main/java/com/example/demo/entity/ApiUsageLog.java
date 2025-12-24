package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.Instant;

@Entity
@Table(name="api_usage_log")
public class ApiUsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Referential integrity: api_key_id FK NOT NULL
    @NotNull
    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="api_key_id", nullable=false)
    @JsonIgnore
    private ApiKey apiKey;

    @NotBlank
    @Column(nullable=false)
    private String endpoint;

    @NotNull
    @PastOrPresent
    @Column(nullable=false)
    private Instant timestamp;

    public Long getId() { return id; }
    public ApiKey getApiKey() { return apiKey; }
    public void setApiKey(ApiKey apiKey) { this.apiKey = apiKey; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}