package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@Entity
@Table(name="rate_limit_enforcement")
public class RateLimitEnforcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Referential integrity: api_key_id FK NOT NULL
    @NotNull
    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="api_key_id", nullable=false)
    @JsonIgnore
    private ApiKey apiKey;

    @NotNull
    @Column(nullable=false)
    private Instant blockedAt;

    @NotNull
    @Min(1)
    @Column(nullable=false)
    private Integer limitExceededBy;

    @NotBlank
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