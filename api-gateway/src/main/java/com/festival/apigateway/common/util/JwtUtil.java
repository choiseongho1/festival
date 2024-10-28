package com.festival.apigateway.common.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
<<<<<<< HEAD
import org.springframework.stereotype.Component;
import java.util.Date;
import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "VlwEyVBsYt9V7zq57TejMnVUyzblYcfPQye08f7MGVA9XkHa\n";
    private final SecretKey key;
=======
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
@Component
public class JwtUtil {
    private final String SECRET_KEY = "VlwEyVBsYt9V7zq57TejMnVUyzblYcfPQye08f7MGVA9XkHa\n";
>>>>>>> e97c9392a588289923946d6d6a7f80e718dde0b2

    static final int ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15;
    static final int REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7;

<<<<<<< HEAD
    public JwtUtil() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
=======
>>>>>>> e97c9392a588289923946d6d6a7f80e718dde0b2

    public String generateToken(String username, String tokenType) {
        int expirationTime = getExpirationTime(tokenType);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
<<<<<<< HEAD
                .signWith(key, SignatureAlgorithm.HS256)
=======
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
>>>>>>> e97c9392a588289923946d6d6a7f80e718dde0b2
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
<<<<<<< HEAD
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
=======
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
>>>>>>> e97c9392a588289923946d6d6a7f80e718dde0b2
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
<<<<<<< HEAD
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
=======
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
>>>>>>> e97c9392a588289923946d6d6a7f80e718dde0b2
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
<<<<<<< HEAD

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
=======
>>>>>>> e97c9392a588289923946d6d6a7f80e718dde0b2
}
