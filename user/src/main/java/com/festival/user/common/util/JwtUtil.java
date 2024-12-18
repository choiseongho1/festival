package com.festival.user.common.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "your_secret_key";
    static final int ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15;
    static final int REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7;


    public String generateToken(String username, String tokenType) {
        int expirationTime = getExpirationTime(tokenType);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
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
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
