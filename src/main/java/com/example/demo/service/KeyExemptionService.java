package com.example.demo.service;

import com.example.demo.entity.KeyExemption;

import java.util.List;

public interface KeyExemptionService {

    KeyExemption createExemption(KeyExemption ex);

    KeyExemption updateExemption(Long apiKeyId, KeyExemption ex);

    KeyExemption getExemptionByKey(Long apiKeyId);

    List<KeyExemption> getAllExemptions();
}