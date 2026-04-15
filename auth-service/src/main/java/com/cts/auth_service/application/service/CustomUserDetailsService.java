package com.cts.auth_service.application.service;

import com.cts.auth_service.application.feign.IamServiceFeignClient;
import com.cts.dto.response.UserAuthDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@AllArgsConstructor
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final IamServiceFeignClient iamServiceFeignClient;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAuthDto userAuthDto = iamServiceFeignClient.findAppUserByEmail(email);
        log.debug("User found: {}, Role: {}", email, userAuthDto.getRoleName());

        String roleName = userAuthDto.getRoleName() != null ? userAuthDto.getRoleName() : "STUDENT";
        String authority = "ROLE_" + roleName;
        log.debug("Authority assigned: {}", authority);

        return new User(
                userAuthDto.getUserEmail(),
                userAuthDto.getUserPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(authority))
        );
    }
}


