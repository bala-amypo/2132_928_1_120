package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ApiKeyUpdateDto {

    @NotNull
    @Positive
    private Long planId;

    private Boolean active;

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}