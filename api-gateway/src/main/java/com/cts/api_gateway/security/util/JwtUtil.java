package com.cts.api_gateway.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtil {

    private static final String SECRET_KEY = "mySecretKeyForJwtTokenGenerationWhichIsLongEnoughToBeSecure123456789";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String extractUsername(String token) {
        log.debug("Extracting username from JWT token");
        String username = extractClaim(token, Claims::getSubject);
        log.info("Successfully extracted username: {}", username);
        return username;
    }

    public String extractRole(String token) {
        log.debug("Extracting role from JWT token");
        String role = extractClaim(token, claims -> claims.get("role", String.class));
        log.info("Successfully extracted role: {}", role);
        return role;
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        log.debug("Extracting claim from JWT token");
        final Claims claims = extractAllClaims(token);
        log.debug("Claims extracted successfully. Available claims: subject={}, issued_at={}, expiration={}",
            claims.getSubject(), claims.getIssuedAt(), claims.getExpiration());
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        log.debug("Parsing JWT token with signing key verification");
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            log.debug("✓ Token signature verified successfully");
            return claims;
        } catch (Exception e) {
            log.error("Failed to parse JWT token. Exception: {}", e.getMessage());
            throw e;
        }
    }

    public Boolean validateToken(String token) {
        log.info("[TOKEN VALIDATION STARTED]");
        log.debug("Token length: {} characters", token.length());

        try {
            log.debug("Step 1: Checking token expiration");
            boolean isExpired = isTokenExpired(token);

            if (isExpired) {
                log.warn("✗ Token validation FAILED - Token is expired");
                log.info("[TOKEN VALIDATION FAILED - TOKEN EXPIRED]");
                return false;
            }

            log.info("✓ Step 1 PASSED - Token is NOT expired");

            log.debug("Step 2: Parsing token to extract claims");
            Claims claims = extractAllClaims(token);
            log.info("✓ Step 2 PASSED - Token claims parsed successfully");

            log.debug("Step 3: Validating token signature");
            log.info("✓ Step 3 PASSED - Token signature is valid");

            log.debug("Step 4: Extracting and logging token details");
            String username = claims.getSubject();
            String role = (String) claims.get("role");
            Date issuedAt = claims.getIssuedAt();
            Date expiresAt = claims.getExpiration();

            log.info("Token Details:");
            log.info("  - Username: {}", username);
            log.info("  - Role: {}", role);
            log.info("  - Issued At: {}", issuedAt);
            log.info("  - Expires At: {}", expiresAt);
            log.info("  - Time Remaining: {} seconds", (expiresAt.getTime() - System.currentTimeMillis()) / 1000);

            log.info("✓ [TOKEN VALIDATION SUCCESSFUL - TOKEN IS VALID]");
            return true;

        } catch (Exception e) {
            log.error("✗ Exception during token validation: {}", e.getClass().getSimpleName());
            log.error("Exception message: {}", e.getMessage());
            log.debug("Exception stacktrace:", e);
            log.info("[TOKEN VALIDATION FAILED - EXCEPTION OCCURRED]");
            return false;
        }
    }

    private Boolean isTokenExpired(String token) {
        log.debug("Checking token expiration time");
        Date expirationDate = extractExpiration(token);
        Date now = new Date();
        boolean isExpired = expirationDate.before(now);

        log.debug("Expiration check details:");
        log.debug("  - Token expires at: {}", expirationDate);
        log.debug("  - Current time: {}", now);
        log.debug("  - Is expired: {}", isExpired);

        return isExpired;
    }

    public Date extractExpiration(String token) {
        log.debug("Extracting expiration date from JWT token");
        Date expiration = extractClaim(token, Claims::getExpiration);
        log.debug("Token expiration date: {}", expiration);
        return expiration;
    }
}
