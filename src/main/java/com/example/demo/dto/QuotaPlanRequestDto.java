package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
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
}