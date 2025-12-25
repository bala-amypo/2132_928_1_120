package com.example.demo.service;

import com.example.demo.entity.KeyExemption;

public interface KeyExemptionService {
    KeyExemption createExemption(KeyExemption ex);
    KeyExemption getExemptionByKey(Long apiKeyId);
}