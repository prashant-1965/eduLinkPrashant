package com.cts.faculty_service.application.feign;

import com.cts.dto.request.AppUserRegistrationDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "iam-service")
public interface AppUserFeign {
    @PostMapping("/appUser/register")
    ResponseEntity<Long> appUserRegistration(@Valid @RequestBody AppUserRegistrationDto appUserRegistrationDto);
}
