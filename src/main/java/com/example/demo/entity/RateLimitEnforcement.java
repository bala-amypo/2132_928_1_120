package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "rate_limit_enforcement")
public class RateLimitEnforcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "api_key_id", nullable = false)
    private ApiKey apiKey;

    @Column(nullable = false)
    private Instant blockedAt;

    @Column(nullable = false)
    private Integer limitExceededBy;

    @Column(nullable = false)
    private String message;

    public Long getId() { return id; }
    public ApiKey getApiKey() { return apiKey; }
    public Instant getBlockedAt() { return blockedAt; }
    public Integer getLimitExceededBy() { return limitExceededBy; }
    public String getMessage() { return message; }

    public void setId(Long id) { this.id = id; }
    public void setApiKey(ApiKey apiKey) { this.apiKey = apiKey; }
    public void setBlockedAt(Instant blockedAt) { this.blockedAt = blockedAt; }
    public void setLimitExceededBy(Integer limitExceededBy) { this.limitExceededBy = limitExceededBy; }
    public void setMessage(String message) { this.message = message; }
}