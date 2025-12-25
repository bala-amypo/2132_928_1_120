package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class KeyExemption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private ApiKey apiKey;

    // test uses setTemporaryExtensionLimit(int)
    private int temporaryExtensionLimit;

    private Instant validUntil;

    // extra for compatibility
    private boolean exempted = true;
    private String reason;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ApiKey getApiKey() { return apiKey; }
    public void setApiKey(ApiKey apiKey) { this.apiKey = apiKey; }

    public int getTemporaryExtensionLimit() { return temporaryExtensionLimit; }
    public void setTemporaryExtensionLimit(int temporaryExtensionLimit) { this.temporaryExtensionLimit = temporaryExtensionLimit; }

    public Instant getValidUntil() { return validUntil; }
    public void setValidUntil(Instant validUntil) { this.validUntil = validUntil; }

    public boolean isExempted() { return exempted; }
    public void setExempted(boolean exempted) { this.exempted = exempted; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}