package com.cts.iam_service.application.service;

import com.cts.dto.request.AppUserRegistrationDto;
import com.cts.dto.request.StudentRegistrationDto;

public interface IAppUserService {
    Long appUserRegistration(AppUserRegistrationDto appUserRegistrationDto);
}
