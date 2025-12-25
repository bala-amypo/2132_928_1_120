package com.example.demo.mapper;

import com.example.demo.dto.*;
import com.example.demo.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public class DtoMapper {

    private DtoMapper() {}

    // -------- QuotaPlan --------
    public static QuotaPlan toEntity(QuotaPlanDto dto) {
        if (dto == null) return null;
        QuotaPlan e = new QuotaPlan();
        e.setId(dto.getId());
        e.setPlanName(dto.getPlanName());
        e.setQuotaLimit(dto.getQuotaLimit());
        e.setTimeWindowSeconds(dto.getTimeWindowSeconds());
        return e;
    }

    public static QuotaPlanDto toDto(QuotaPlan e) {
        if (e == null) return null;
        QuotaPlanDto dto = new QuotaPlanDto();
        dto.setId(e.getId());
        dto.setPlanName(e.getPlanName());
        dto.setQuotaLimit(e.getQuotaLimit());
        dto.setTimeWindowSeconds(e.getTimeWindowSeconds());
        return dto;
    }

    public static List<QuotaPlanDto> toQuotaPlanDtos(List<QuotaPlan> list) {
        return list.stream().map(DtoMapper::toDto).collect(Collectors.toList());
    }

    // -------- ApiKey --------
    public static ApiKey toEntity(ApiKeyDto dto) {
        if (dto == null) return null;
        ApiKey e = new ApiKey();
        e.setId(dto.getId());
        e.setApiKey(dto.getApiKey());
        e.setOwnerEmail(dto.getOwnerEmail());
        e.setActive(dto.isActive());
        return e;
    }

    public static ApiKeyDto toDto(ApiKey e) {
        if (e == null) return null;
        ApiKeyDto dto = new ApiKeyDto();
        dto.setId(e.getId());
        dto.setApiKey(e.getApiKey());
        dto.setOwnerEmail(e.getOwnerEmail());
        dto.setActive(e.isActive());
        return dto;
    }

    public static List<ApiKeyDto> toApiKeyDtos(List<ApiKey> list) {
        return list.stream().map(DtoMapper::toDto).collect(Collectors.toList());
    }

    // -------- ApiUsageLog --------
    public static ApiUsageLog toEntity(ApiUsageLogDto dto) {
        if (dto == null) return null;
        ApiUsageLog e = new ApiUsageLog();
        e.setId(dto.getId());
        e.setRequestTimestamp(dto.getRequestTimestamp());
        e.setEndpoint(dto.getEndpoint());
        e.setSuccess(dto.isSuccess());
        e.setResponseCode(dto.getResponseCode());
        return e;
    }

    public static ApiUsageLogDto toDto(ApiUsageLog e) {
        if (e == null) return null;
        ApiUsageLogDto dto = new ApiUsageLogDto();
        dto.setId(e.getId());
        dto.setRequestTimestamp(e.getRequestTimestamp());
        dto.setEndpoint(e.getEndpoint());
        dto.setSuccess(e.isSuccess());
        dto.setResponseCode(e.getResponseCode());
        return dto;
    }

    public static List<ApiUsageLogDto> toUsageDtos(List<ApiUsageLog> list) {
        return list.stream().map(DtoMapper::toDto).collect(Collectors.toList());
    }

    // -------- RateLimitEnforcement --------
    public static RateLimitEnforcement toEntity(RateLimitEnforcementDto dto) {
        if (dto == null) return null;
        RateLimitEnforcement e = new RateLimitEnforcement();
        e.setId(dto.getId());
        e.setBlocked(dto.isBlocked());
        e.setReason(dto.getReason());
        e.setBlockedUntil(dto.getBlockedUntil());
        return e;
    }

    public static RateLimitEnforcementDto toDto(RateLimitEnforcement e) {
        if (e == null) return null;
        RateLimitEnforcementDto dto = new RateLimitEnforcementDto();
        dto.setId(e.getId());
        dto.setBlocked(e.isBlocked());
        dto.setReason(e.getReason());
        dto.setBlockedUntil(e.getBlockedUntil());
        return dto;
    }

    // -------- KeyExemption --------
    public static KeyExemption toEntity(KeyExemptionDto dto) {
        if (dto == null) return null;
        KeyExemption e = new KeyExemption();
        e.setId(dto.getId());
        e.setExempted(dto.isExempted());
        e.setReason(dto.getReason());
        return e;
    }

    public static KeyExemptionDto toDto(KeyExemption e) {
        if (e == null) return null;
        KeyExemptionDto dto = new KeyExemptionDto();
        dto.setId(e.getId());
        dto.setExempted(e.isExempted());
        dto.setReason(e.getReason());
        return dto;
    }
}