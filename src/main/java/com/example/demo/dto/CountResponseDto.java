package com.example.demo.dto;

public class CountResponseDto {
    private long count;
    public CountResponseDto(long count) { this.count = count; }
    public long getCount() { return count; }
}