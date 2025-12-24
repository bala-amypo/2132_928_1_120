package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class QuotaPlanRequestDto {
    @NotBlank private String planName;
    @NotNull @Min(1) private Integer dailyLimit;
    private String description;
    private Boolean active = true;

    public String getPlanName() { return planName; }
    public Integer getDailyLimit() { return dailyLimit; }
    public String getDescription() { return description; }
    public Boolean getActive() { return active; }

    public void setPlanName(String planName) { this.planName = planName; }
    public void setDailyLimit(Integer dailyLimit) { this.dailyLimit = dailyLimit; }
    public void setDescription(String description) { this.description = description; }
    public void setActive(Boolean active) { this.active = active; }
}