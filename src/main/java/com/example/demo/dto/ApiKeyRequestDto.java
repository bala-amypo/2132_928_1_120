package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ApiKeyRequestDto {
    @NotBlank private String keyValue;
    @NotNull private Long ownerId;
    @NotNull private Long planId;
    private Boolean active = true;

    public String getKeyValue() { return keyValue; }
    public Long getOwnerId() { return ownerId; }
    public Long getPlanId() { return planId; }
    public Boolean getActive() { return active; }

    public void setKeyValue(String keyValue) { this.keyValue = keyValue; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public void setActive(Boolean active) { this.active = active; }
}