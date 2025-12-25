package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "key_exemptions")
public class KeyExemption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "api_key_id", nullable = false, unique = true)
    private ApiKey apiKey;

    @Column(name = "exempted", nullable = false)
    private boolean exempted;

    @Column(name = "reason", length = 500)
    private String reason;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ApiKey getApiKey() { return apiKey; }
    public void setApiKey(ApiKey apiKey) { this.apiKey = apiKey; }

    public boolean isExempted() { return exempted; }
    public void setExempted(boolean exempted) { this.exempted = exempted; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}