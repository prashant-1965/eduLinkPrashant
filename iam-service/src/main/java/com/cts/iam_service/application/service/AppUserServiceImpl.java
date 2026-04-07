package com.cts.iam_service.application.service;

import com.cts.classexception.AppUserException;
import com.cts.dto.request.AppUserRegistrationDto;
import com.cts.iam_service.application.entity.AppUser;
import com.cts.iam_service.application.entity.Role;
import com.cts.iam_service.application.repository.AppUserRepository;
import com.cts.iam_service.application.repository.RoleRepository;
import com.cts.iam_service.application.util.DtoMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class AppUserServiceImpl implements IAppUserService{
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;

    @Transactional
    @Override
    @Retry(name = "appUserRegistration", fallbackMethod = "appUserFallback")
    @CircuitBreaker(name = "appUserRegistration", fallbackMethod = "appUserFallback")
    public Long appUserRegistration(AppUserRegistrationDto appUserRegistrationDto) throws AppUserException {
        AppUser appUser = DtoMapper.appUserDtoSeparator(appUserRegistrationDto);
        log.info("AppUser registration intercepted ");
        log.debug("AppUserRepo initiated searching user by email");
        Optional<AppUser> appUserOptional = appUserRepository.findAppUserByUserEmail(appUser.getUserEmail());
        if(appUserOptional.isPresent()){
            log.error("{}'s email is Already present into database with email {}",appUser.getUserName(),appUser.getUserEmail());
            throw new AppUserException("Your "+appUser.getUserEmail()+" is Already registered", HttpStatus.CONFLICT);
        }
        log.debug("AppUserRepo initiated searching user by phone number");
        Optional<AppUser> appUserOptional1 = appUserRepository.findAppUserByUserPhoneNumber(appUser.getPhoneNumber());
        if(appUserOptional1.isPresent()){
            log.error("{}'s phone number is Already present into database with phone number {}",appUser.getUserName(),appUser.getPhoneNumber());
            throw new AppUserException("Your "+appUser.getPhoneNumber()+" is Already registered", HttpStatus.CONFLICT);
        }
        log.info("Extraction completed for student and user entities from DTO");
        Optional<Role> role = roleRepository.findRoleByName(appUserRegistrationDto.getRole());
        if(role.isPresent()){
            appUser.setRole(role.get());
        } else {
            throw new AppUserException("Role not found", HttpStatus.BAD_REQUEST);
        }
        appUserRepository.save(appUser);
        log.info("AppUser registration successful for email {}",appUser.getUserEmail());
        return appUser.getId();
    }
    public Long appUserFallback(AppUserRegistrationDto appUserRegistrationDto, Throwable t) throws AppUserException {
        log.error("AppUser Fallback triggered for {}. Reason: {}",
                appUserRegistrationDto.getUserEmail(), t.getMessage());
        if (t instanceof AppUserException) {
            throw (AppUserException) t;
        }
        throw new AppUserException("User service is currently unavailable. Please try later.", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
