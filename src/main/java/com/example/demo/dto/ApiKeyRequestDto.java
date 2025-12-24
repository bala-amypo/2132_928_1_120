package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ApiKeyRequestDto {

    @NotBlank
    private String keyValue;

    @NotNull
    private Long ownerId;

    @NotNull
    private Long planId;

    @NotNull
    private Boolean active;

    public String getKeyValue() { return keyValue; }
    public void setKeyValue(String keyValue) { this.keyValue = keyValue; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}