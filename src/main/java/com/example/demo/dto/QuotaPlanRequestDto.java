package com.example.demo.dto;

import jakarta.validation.constraints.*;

public class QuotaPlanRequestDto {

    @NotBlank(message = "planName is required")
    @Size(min = 2, max = 100, message = "planName must be 2 to 100 chars")
    private String planName;

    @NotNull(message = "dailyLimit is required")
    @Min(value = 1, message = "dailyLimit must be >= 1")
    private Long dailyLimit;

    @Size(max = 255, message = "description max 255 chars")
    private String description;

    @NotNull(message = "active is required")
    private Boolean active;

    public String getPlanName() { return planName; }
    public Long getDailyLimit() { return dailyLimit; }
    public String getDescription() { return description; }
    public Boolean getActive() { return active; }

    public void setPlanName(String planName) { this.planName = planName; }
    public void setDailyLimit(Long dailyLimit) { this.dailyLimit = dailyLimit; }
    public void setDescription(String description) { this.description = description; }
    public void setActive(Boolean active) { this.active = active; }
}