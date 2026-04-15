package com.cts.api_gateway.security.filter;

import com.cts.api_gateway.security.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Component
@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    private Mono<Void> sendUnauthorizedResponse(ServerWebExchange exchange, String errorMessage, String errorCode) {
        log.debug("Sending unauthorized response: {} ({})", errorMessage, errorCode);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        HashMap<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", errorCode);
        errorResponse.put("message", errorMessage);
        errorResponse.put("timestamp", System.currentTimeMillis());
        errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());

        try {
            byte[] responseBody = objectMapper.writeValueAsBytes(errorResponse);
            return exchange.getResponse().writeWith(
                    Mono.fromCallable(() -> exchange.getResponse().bufferFactory().wrap(responseBody))
            );
        } catch (Exception e) {
            log.error("Failed to serialize error response", e);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        log.info("Processing request for path: {}", path);

        // Skip authentication for login and register endpoints
        if (path.startsWith("/auth/login") || path.startsWith("/appUser/register") ||
            path.startsWith("/student/register") || path.startsWith("/faculty/register")) {
            log.info("Public endpoint detected, skipping authentication for path: {}", path);
            return chain.filter(exchange);
        }

        log.debug("Protected endpoint detected, proceeding with JWT validation");
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.error("Authorization header is missing or does not start with 'Bearer '. Path: {}", path);
            log.error("Authorization header value: {}", authHeader);
            return sendUnauthorizedResponse(exchange, "Authorization header is missing or invalid", "UNAUTHORIZED_NO_TOKEN");
        }

        log.debug("Authorization header found, extracting token");
        String token = authHeader.substring(7);
        log.debug("Token extracted, length: {} characters", token.length());

        try {
            log.debug("Attempting to validate JWT token");
            Boolean isTokenValid = jwtUtil.validateToken(token);
            log.debug("Token validation result: {}", isTokenValid);

            if (!isTokenValid) {
                log.error("JWT token validation failed or token is expired. Path: {}", path);
                return sendUnauthorizedResponse(exchange, "JWT token is invalid or expired", "UNAUTHORIZED_INVALID_TOKEN");
            }

            log.info("✓ JWT token validation SUCCESSFUL");
            String username;
            String role;

            try {
                username = jwtUtil.extractUsername(token);
                role = jwtUtil.extractRole(token);
            } catch (Exception extractionException) {
                log.error("Failed to extract claims from token. Exception: {}", extractionException.getMessage(), extractionException);
                return sendUnauthorizedResponse(exchange, "Failed to extract user details from token", "UNAUTHORIZED_EXTRACTION_FAILED");
            }

            if (username == null || role == null) {
                log.error("Username or role is null after extraction. Username: {}, Role: {}", username, role);
                return sendUnauthorizedResponse(exchange, "Token does not contain valid user details", "UNAUTHORIZED_NULL_CLAIMS");
            }

            log.info("Token matched and decoded successfully");
            log.debug("Role-based authentication - User role: {} - Type: {}", role, role.getClass().getName());

            log.info("Setting authentication headers for downstream services");
            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(exchange.getRequest().mutate()
                            .header("X-User-Email", username)
                            .header("X-User-Role", role)
                            .build())
                    .build();

            log.info("✓ All headers configured successfully for path: {}", path);
            log.info("JWT AUTHENTICATION FILTER END (AUTHORIZED)");

            return chain.filter(modifiedExchange);
        } catch (Exception e) {
            log.error("Exception occurred during JWT token processing for path: {}. Exception type: {}", path, e.getClass().getName());
            log.error("Exception message: {}", e.getMessage());
            log.error("Full exception stacktrace: ", e);
            return sendUnauthorizedResponse(exchange, "Internal server error during token processing", "UNAUTHORIZED_EXCEPTION");
        }
    }
}
