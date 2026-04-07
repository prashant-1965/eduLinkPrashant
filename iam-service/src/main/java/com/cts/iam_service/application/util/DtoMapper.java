package com.cts.iam_service.application.util;

import com.cts.dto.request.AppUserRegistrationDto;
import com.cts.iam_service.application.entity.AppUser;

public class DtoMapper {
    public static AppUser appUserDtoSeparator(AppUserRegistrationDto appUserDto){
        AppUser appUser = new AppUser();
        appUser.setUserEmail(appUserDto.getUserEmail());
        appUser.setUserName(appUserDto.getUserName());
        appUser.setPhoneNumber(appUserDto.getPhoneNumber());
//        String encodePassword = passwordEncoder.encode(appUserDto.getPassword());
//        appUser.setUserPassword(encodePassword);
        return appUser;
    }
}
