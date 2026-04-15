package com.cts.iam_service.application.controller;

import com.cts.dto.request.AppUserRegistrationDto;
import com.cts.dto.response.AppUserDetailByIdDto;
import com.cts.dto.response.UserAuthDto;
import com.cts.iam_service.application.service.IAppUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appUser")
@AllArgsConstructor
@Slf4j
public class AppUserController {

    private final IAppUserService appUserService;

    @PostMapping("/register")
    public ResponseEntity<Long> appUserRegistration(@Valid @RequestBody AppUserRegistrationDto appUserRegistrationDto){
        log.info("App user registration request has been initiated successFully by {}",appUserRegistrationDto.getUserName());
        return ResponseEntity.status(200).body(appUserService.appUserRegistration(appUserRegistrationDto));
    }

    @GetMapping("/findAppUserNameByAppUserId/{appUserId}")
    public String findAppUserNameByAppUserId(@PathVariable Long appUserId){
        log.info("Request received to find app user name for app user id: {}", appUserId);
        return appUserService.findAppUserNameByAppUserId(appUserId);
    }

    @GetMapping("/findAppUserDetailsByAppUserId/{appUserId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'FACULTY', 'ADMIN')")
    public AppUserDetailByIdDto findAppUserDetailsByAppUserId(@PathVariable Long appUserId){
        log.info("Request received to find app user details for app user id: {}", appUserId);
        return appUserService.findAppUserDetailsByAppUserId(appUserId);
    }

    @GetMapping("/findAppUserByEmail/{email}")
    public ResponseEntity<UserAuthDto> findAppUserByEmail(@PathVariable String email){
        log.info("Request received to find app user by email: {}", email);
        UserAuthDto userAuthDto = appUserService.findAppUserByEmail(email);
        return ResponseEntity.ok(userAuthDto);
    }
}