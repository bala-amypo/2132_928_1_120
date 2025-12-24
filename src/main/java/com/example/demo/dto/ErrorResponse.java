package com.example.demo.dto;

import java.time.Instant;

public class ErrorResponse {
    private String errorCode;
    private String errorMessage;
    private Instant timestamp = Instant.now();

    public ErrorResponse(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public Instant getTimestamp() { return timestamp; }
}