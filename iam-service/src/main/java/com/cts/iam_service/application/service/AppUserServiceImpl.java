package com.cts.iam_service.application.service;

import com.cts.classexception.AppUserException;
import com.cts.dto.request.AppUserRegistrationDto;
import com.cts.dto.response.AppUserDetailByIdDto;
import com.cts.dto.response.UserAuthDto;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class AppUserServiceImpl implements IAppUserService{
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    @Retry(name = "appUserRegistration", fallbackMethod = "appUserFallback")
    @CircuitBreaker(name = "appUserRegistration", fallbackMethod = "appUserFallback")
    public Long appUserRegistration(AppUserRegistrationDto appUserRegistrationDto) throws AppUserException {
        AppUser appUser = DtoMapper.appUserDtoSeparator(appUserRegistrationDto, passwordEncoder);
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

    @Override
    public String findAppUserNameByAppUserId(Long appUserId) throws AppUserException {
        Optional<AppUser> appUser = appUserRepository.findById(appUserId);
        if(appUser.isEmpty()){
            log.error("AppUser not found for ID: {}", appUserId);
            throw new AppUserException("AppUser not found for ID: " + appUserId, HttpStatus.NOT_FOUND);
        }
        return appUser.get().getUserName();
    }

    @Override
    public AppUserDetailByIdDto findAppUserDetailsByAppUserId(Long appUserId) throws AppUserException{
        Optional<AppUser> appUser = appUserRepository.findById(appUserId);
        if(appUser.isEmpty()){
            log.error("AppUser not found for Id: {}", appUserId);
            throw new AppUserException("AppUser not found for ID: " + appUserId, HttpStatus.NOT_FOUND);
        }
        return DtoMapper.appUserToAppUserDetailById(appUser.get());
    }

    @Override
    public UserAuthDto findAppUserByEmail(String email) throws AppUserException {
        Optional<AppUser> appUser = appUserRepository.findAppUserByUserEmail(email);
        if(appUser.isEmpty()){
            log.error("AppUser not found for email: {}", email);
            throw new AppUserException("User not found for email: " + email, HttpStatus.NOT_FOUND);
        }
        UserAuthDto userAuthDto = new UserAuthDto();
        userAuthDto.setId(appUser.get().getId());
        userAuthDto.setUserName(appUser.get().getUserName());
        userAuthDto.setUserEmail(appUser.get().getUserEmail());
        userAuthDto.setUserPassword(appUser.get().getUserPassword());
        userAuthDto.setRoleName(appUser.get().getRole() != null ? appUser.get().getRole().getRoleName() : null);
        log.info("Retrieved user details for email: {}", email);
        return userAuthDto;
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
