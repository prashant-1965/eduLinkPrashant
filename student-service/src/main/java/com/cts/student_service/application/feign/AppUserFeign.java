package com.cts.student_service.application.feign;

import com.cts.dto.request.StudentRegistrationDto;
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
    ResponseEntity<Long> appUserRegistration(@Valid @RequestBody StudentRegistrationDto studentRegistrationDto);
    @GetMapping("/appUser/findAppUserNameByAppUserId/{appUserId}")
    String findAppUserNameByAppUserId(@PathVariable Long appUserId);
}
