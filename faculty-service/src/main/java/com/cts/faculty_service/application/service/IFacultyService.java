package com.cts.faculty_service.application.service;

import com.cts.dto.request.FacultyRegistrationDto;
import com.cts.dto.response.FacultyDetailProjection;

public interface IFacultyService {
    String registerFaculty(FacultyRegistrationDto facultyRegistrationDto);
    void checkFacultyByFacultyId(Long facultyId);
    FacultyDetailProjection getFacultyDetailsByFacultyId(Long facultyId);
}
