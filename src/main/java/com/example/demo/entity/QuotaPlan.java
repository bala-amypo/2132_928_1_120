package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "quota_plan")
public class QuotaPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String planName;

    @Column(nullable = false)
    private Long dailyLimit;

    private String description;

    @Column(nullable = false)
    private Boolean active = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public Long getDailyLimit() { return dailyLimit; }
    public void setDailyLimit(Long dailyLimit) { this.dailyLimit = dailyLimit; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}