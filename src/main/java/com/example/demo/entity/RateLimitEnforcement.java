package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Table(name = "rate_limit_enforcement")
public class RateLimitEnforcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "apiKey is required")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "api_key_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_enforcement_api_key")
    )
    @OnDelete(action = OnDeleteAction.CASCADE) // ✅ delete enforcement rows if ApiKey deleted
    @JsonIgnore
    private ApiKey apiKey;

    @NotNull(message = "blockedAt is required")
    @Column(nullable = false)
    private Instant blockedAt;

    @NotNull(message = "limitExceededBy is required")
    @Min(value = 1, message = "limitExceededBy must be >= 1")
    @Column(nullable = false)
    private Integer limitExceededBy;

    @NotBlank(message = "message is required")
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