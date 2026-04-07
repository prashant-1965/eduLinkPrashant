package com.cts.faculty_service.application.service;

import com.cts.dto.request.FacultyRegistrationDto;

public interface IFacultyService {
    String registerFaculty(FacultyRegistrationDto facultyRegistrationDto);
}
