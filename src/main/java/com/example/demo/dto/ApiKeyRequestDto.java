package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ApiKeyRequestDto {

    @NotBlank
    @Size(min = 5, max = 128)
    private String keyValue;

    @NotNull
    @Positive
    private Long ownerId;

    @NotNull
    @Positive
    private Long planId;   // ✅ IMPORTANT

    private Boolean active = true;

    public String getKeyValue() { return keyValue; }
    public void setKeyValue(String keyValue) { this.keyValue = keyValue; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}