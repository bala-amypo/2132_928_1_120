package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final Key key;
    private final long expirationMillis;

    public JwtUtil(
            @Value("${jwt.secret:MySuperSecretKeyForJwtSigning_ChangeThis_1234567890}") String secret,
            @Value("${jwt.expiration:3600000}") long expiration,
            @Value("${jwt.expiration-ms:0}") long expirationMs // optional fallback
    ) {
        // prefer jwt.expiration-ms if set (>0), else use jwt.expiration
        this.expirationMillis = (expirationMs > 0) ? expirationMs : expiration;

        // JJWT requires >= 32 chars for HS256; your secret already looks long enough
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(secretBytes);
    }

    public String generateToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String expectedUsername) {
        try {
            Claims c = getClaims(token);
            boolean notExpired = c.getExpiration() != null && c.getExpiration().after(new Date());
            return expectedUsername != null && expectedUsername.equals(c.getSubject()) && notExpired;
        } catch (Exception e) {
            return false;
        }
    }

    public long getExpirationMillis() {
        return expirationMillis;
    }
}