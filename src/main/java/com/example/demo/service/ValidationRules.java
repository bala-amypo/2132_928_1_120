package com.example.demo.service;

import com.example.demo.entity.KeyExemption;
import com.example.demo.entity.QuotaPlan;
import com.example.demo.exception.BadRequestException;

import java.time.Instant;

public final class ValidationRules {
    private ValidationRules() {}

    public static void requireActivePlan(QuotaPlan plan) {
        if (plan == null) throw new BadRequestException("Quota plan is required");

        Boolean active = plan.getActive();
        if (active == null || !active) {
            throw new BadRequestException("Quota plan is inactive");
        }

        Integer limit = plan.getDailyLimit();
        if (limit == null || limit <= 0) {
            throw new BadRequestException("Quota plan dailyLimit must be > 0");
        }
    }

    public static void validateExemption(KeyExemption ex) {
        if (ex == null) return;

        if (ex.getTemporaryExtensionLimit() != null && ex.getTemporaryExtensionLimit() < 0) {
            throw new BadRequestException("temporaryExtensionLimit must be >= 0");
        }

        if (ex.getValidUntil() != null && !ex.getValidUntil().isAfter(Instant.now())) {
            throw new BadRequestException("validUntil must be in the future");
        }
    }
}