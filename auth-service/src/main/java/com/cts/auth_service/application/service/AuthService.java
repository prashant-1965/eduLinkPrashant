package com.cts.auth_service.application.service;

import com.cts.auth_service.application.feign.IamServiceFeignClient;
import com.cts.auth_service.security.util.JwtUtil;
import com.cts.dto.request.LoginDto;
import com.cts.dto.response.LoginResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final IamServiceFeignClient iamServiceFeignClient;

    public LoginResponseDto login(LoginDto loginDto) {
        log.info("Login attempt initiated for email: {}", loginDto.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getEmail());
        String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority().replace("ROLE_", "");
        log.debug("Fetching user details from IAM Service for email: {}", loginDto.getEmail());
        Long userId = iamServiceFeignClient.findAppUserByEmail(loginDto.getEmail()).getId();
        String userName = iamServiceFeignClient.findAppUserNameByAppUserId(userId);

        log.info("Generating JWT token for user: {}", userName);
        String token = jwtUtil.generateToken(userDetails, role);

        LoginResponseDto response = new LoginResponseDto();
        response.setToken(token);
        log.info("Login successful for user: {}", loginDto.getEmail());
        return response;
    }
}
