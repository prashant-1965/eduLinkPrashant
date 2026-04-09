package com.cts.faculty_service.application.feign;

import com.cts.dto.request.AppUserRegistrationDto;
import com.cts.dto.response.AppUserDetailByIdDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "iam-service")
public interface AppUserFeign {
    @PostMapping("/appUser/register")
    ResponseEntity<Long> appUserRegistration(@Valid @RequestBody AppUserRegistrationDto appUserRegistrationDto);

    @GetMapping("/appUser/findAppUserNameByAppUserId/{appUserId}")
    String findAppUserNameByAppUserId(@PathVariable Long appUserId);

    @GetMapping("/appUser/findAppUserDetailsByAppUserId/{appUserId}")
    AppUserDetailByIdDto findAppUserDetailsByAppUserId(@PathVariable Long appUserId);
}
