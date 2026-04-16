package com.cts.enrollment_service.security.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        boolean isPublicPath = path.startsWith("/faculty-course-assignment/findCourseListByFacultyId/") ||
                path.startsWith("/faculty-course-assignment/findFacultyIdByCourseId/") ||
                path.startsWith("/faculty-course-assignment/getCourseCountByFacultyId/") ||
                path.startsWith("/student-course-assignment/findCourseListBystudentId/") ||
                path.startsWith("/student-course-assignment/checkEnrollment/") ||
                path.startsWith("/actuator/");

        if (isPublicPath) {
            log.debug("Skipping JWT filter for public path: {}", path);
        }
        return isPublicPath;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        log.debug("Processing request for path: {}", requestPath);

        String userEmail = request.getHeader("X-User-Email");
        String userRole = request.getHeader("X-User-Role");

        log.debug("Checking for authentication headers - Email: {}, Role: {}", userEmail, userRole);

        if (userEmail != null && !userEmail.isEmpty() && userRole != null && !userRole.isEmpty()) {
            log.info("✓ Authentication headers found for user: {}", userEmail);
            log.debug("Setting role: ROLE_{}", userRole);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userEmail,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userRole))
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Authentication set successfully");
        } else {
            log.warn("Authentication headers missing or empty - Email: {}, Role: {}", userEmail, userRole);
            log.warn("Request path: {}", requestPath);
        }

        filterChain.doFilter(request, response);
    }
}

