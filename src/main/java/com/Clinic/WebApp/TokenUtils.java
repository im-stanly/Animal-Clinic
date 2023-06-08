package com.Clinic.WebApp;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {
    private static final long EXPIRATION_TIME = 3600000; // 1 hour

    public static String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("role", role);

        long now = System.currentTimeMillis();
        Date expirationDate = new Date(now + EXPIRATION_TIME);

        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .compact();

        return token;
    }

    public static boolean isTokenValid(String token) {
        try {
            if (token == null || token.isEmpty()) {
                return false;
            }

            Claims claims = Jwts.parser().parseClaimsJws(token).getBody();

            Date expirationDate = claims.getExpiration();
            Date currentDate = new Date();
            if (expirationDate.before(currentDate)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Claims getTokenClaims(String token) {
        try {
            Claims claims = Jwts.parser().parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception e) {
            return null;
        }
    }
}
