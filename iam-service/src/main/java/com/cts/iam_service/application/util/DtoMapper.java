package com.cts.iam_service.application.util;

import com.cts.dto.request.AppUserRegistrationDto;
import com.cts.dto.response.AppUserDetailByIdDto;
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

    public static AppUserDetailByIdDto appUserToAppUserDetailById(AppUser appUser) {
        AppUserDetailByIdDto appUserDetailByIdDto = new AppUserDetailByIdDto();
        appUserDetailByIdDto.setUserEmail(appUser.getUserEmail());
        appUserDetailByIdDto.setUserName(appUser.getUserName());
        appUserDetailByIdDto.setPhoneNumber(appUser.getPhoneNumber());
        return appUserDetailByIdDto;
    }
}
