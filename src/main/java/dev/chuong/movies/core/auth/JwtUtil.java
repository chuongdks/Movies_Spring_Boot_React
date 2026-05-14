package dev.chuong.movies.core.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/* UTILITY
 * Handles all JWT operations:
 *   - generateToken(username) → creates a signed JWT
 *   - extractUsername(token)  → reads username from token
 *   - isTokenValid(token)     → checks signature + expiry
 */
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationMs; // 86400000 = 24 hours

    // Build the signing key from the secret string
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Create a JWT token for a given username
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    // Pull the username (subject) out of a token
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    // Returns true if the token is properly signed and not expired
    public boolean isTokenValid(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false; // tampered, expired, or malformed
        }
    }

    // Internal helper — parses and verifies the token signature
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
