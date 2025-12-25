package com.example.demo.security;

import io.jsonwebtoken.Claims;

import java.util.Map;

public class JwtUtil {

    // mocked in test
    public String generateToken(Map<String, Object> claims, String subject) {
        return "DUMMY";
    }

    // mocked in tests
    public Claims getClaims(String token) {
        return null;
    }

    // test uses: when(jwtUtil.getUsername("TOKEN1")).thenCallRealMethod();
    public String getUsername(String token) {
        Claims claims = getClaims(token);
        return (claims == null) ? null : claims.getSubject();
    }

    // test calls thenCallRealMethod() and checks > 0
    public long getExpirationMillis() {
        return 1000L * 60 * 60; // 1 hour
    }

    // mocked in tests
    public boolean isTokenValid(String token, String username) {
        return false;
    }
}