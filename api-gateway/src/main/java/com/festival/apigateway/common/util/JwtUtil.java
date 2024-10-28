package com.festival.apigateway.common.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.util.Date;
import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "VlwEyVBsYt9V7zq57TejMnVUyzblYcfPQye08f7MGVA9XkHa\n";
    private final SecretKey key;

    static final int ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15;
    static final int REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7;

    public JwtUtil() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String username, String tokenType) {
        int expirationTime = getExpirationTime(tokenType);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private int getExpirationTime(String tokenType) {
        if ("ACCESS".equalsIgnoreCase(tokenType)) {
            return ACCESS_TOKEN_EXPIRATION;
        } else if ("REFRESH".equalsIgnoreCase(tokenType)) {
            return REFRESH_TOKEN_EXPIRATION;
        } else {
            throw new IllegalArgumentException("올바르지 않은 Token Type 입니다.");
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
