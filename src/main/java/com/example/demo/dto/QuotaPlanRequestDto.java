package com.example.demo.dto;

import jakarta.validation.constraints.*;

public class QuotaPlanRequestDto {

    @NotBlank
    @Size(min = 2, max = 80)
    private String planName;

    @NotNull
    @Min(1)
    private Integer dailyLimit;   // ✅ Integer (not Long)

    @Size(max = 255)
    private String description;

    private Boolean active = true;

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public Integer getDailyLimit() { return dailyLimit; }
    public void setDailyLimit(Integer dailyLimit) { this.dailyLimit = dailyLimit; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}