package com.example.demo.dto;

public class ApiKeyUsageSummaryDto {

    private Long apiKeyId;
    private Long totalRequestsToday;
    private Integer dailyLimit;
    private Boolean blocked;

    public ApiKeyUsageSummaryDto(Long apiKeyId,
                                 Long totalRequestsToday,
                                 Integer dailyLimit,
                                 Boolean blocked) {
        this.apiKeyId = apiKeyId;
        this.totalRequestsToday = totalRequestsToday;
        this.dailyLimit = dailyLimit;
        this.blocked = blocked;
    }

    public Long getApiKeyId() { return apiKeyId; }
    public Long getTotalRequestsToday() { return totalRequestsToday; }
    public Integer getDailyLimit() { return dailyLimit; }
    public Boolean getBlocked() { return blocked; }
}