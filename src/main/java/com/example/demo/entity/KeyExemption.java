package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@Entity
@Table(name = "key_exemption")
public class KeyExemption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "apiKey is required")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "api_key_id", nullable = false)
    private ApiKey apiKey;

    private String notes;

    @NotNull(message = "unlimitedAccess is required")
    @Column(nullable = false)
    private Boolean unlimitedAccess = false;

    @Min(value = 0, message = "temporaryExtensionLimit must be >= 0")
    private Integer temporaryExtensionLimit;

    @NotNull(message = "validUntil is required")
    @Future(message = "validUntil must be in the future")
    private Instant validUntil;

    public Long getId() { return id; }
    public ApiKey getApiKey() { return apiKey; }
    public String getNotes() { return notes; }
    public Boolean getUnlimitedAccess() { return unlimitedAccess; }
    public Integer getTemporaryExtensionLimit() { return temporaryExtensionLimit; }
    public Instant getValidUntil() { return validUntil; }

    public void setId(Long id) { this.id = id; }
    public void setApiKey(ApiKey apiKey) { this.apiKey = apiKey; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setUnlimitedAccess(Boolean unlimitedAccess) { this.unlimitedAccess = unlimitedAccess; }
    public void setTemporaryExtensionLimit(Integer temporaryExtensionLimit) { this.temporaryExtensionLimit = temporaryExtensionLimit; }
    public void setValidUntil(Instant validUntil) { this.validUntil = validUntil; }
}