package com.cts.iam_service.application.util;

import com.cts.dto.request.StudentRegistrationDto;
import com.cts.iam_service.application.entity.AppUser;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DtoMapper {
    public static AppUser appUserDtoSeparator(StudentRegistrationDto appUserDto){
        AppUser appUser = new AppUser();
        appUser.setUserEmail(appUserDto.getUserEmail());
        appUser.setUserName(appUserDto.getUserName());
        appUser.setPhoneNumber(appUserDto.getPhoneNumber());
//        String encodePassword = passwordEncoder.encode(appUserDto.getPassword());
//        appUser.setUserPassword(encodePassword);
        return appUser;
    }
}
