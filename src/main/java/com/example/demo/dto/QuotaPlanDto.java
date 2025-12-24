package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class QuotaPlanDto {

    private Long id;

    @NotBlank(message = "planName is required")
    private String planName;

    @NotNull(message = "dailyLimit is required")
    private Long dailyLimit;

    private String description;
    private Boolean active;

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