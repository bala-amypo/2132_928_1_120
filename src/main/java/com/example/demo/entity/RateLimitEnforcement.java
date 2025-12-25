package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name="rate_limit_enforcement")
public class RateLimitEnforcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="api_key_id", nullable=false)
    @JsonIgnore
    private ApiKey apiKey;

    @Column(nullable=false)
    private Instant blockedAt;

    @Column(nullable=false)
    private Integer limitExceededBy;

    @Column(nullable=false)
    private String message;

    public Long getId() { return id; }

    public ApiKey getApiKey() { return apiKey; }
    public void setApiKey(ApiKey apiKey) { this.apiKey = apiKey; }

    public Instant getBlockedAt() { return blockedAt; }
    public void setBlockedAt(Instant blockedAt) { this.blockedAt = blockedAt; }

    public Integer getLimitExceededBy() { return limitExceededBy; }
    public void setLimitExceededBy(Integer limitExceededBy) { this.limitExceededBy = limitExceededBy; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}