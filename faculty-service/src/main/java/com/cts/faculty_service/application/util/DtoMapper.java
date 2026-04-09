package com.cts.faculty_service.application.util;

import com.cts.dto.request.FacultyRegistrationDto;
import com.cts.dto.response.AppUserDetailByIdDto;
import com.cts.dto.response.FacultyDetailByIdDto;
import com.cts.faculty_service.application.entity.Faculty;
import com.cts.util.UIDGeneratorUtils;

public class DtoMapper {
    public static Faculty facultyDtoSeparator(FacultyRegistrationDto facultyRegistrationDto){
        Faculty faculty = new Faculty();
        faculty.setFacultyGender(facultyRegistrationDto.getFacultyGender());
        faculty.setFacultyYearOfExperience(facultyRegistrationDto.getFacultyYearOfExperience());
        faculty.setFacultyAddress(facultyRegistrationDto.getFacultyAddress());
        faculty.setFacultyRating(0.0);
        faculty.setTotalFacultyRatingCount(0L);
        Long facultyId = UIDGeneratorUtils.uidGenerator();
        faculty.setFacultyId(facultyId);
        return faculty;
    }

    public static FacultyDetailByIdDto facultyAppUserMapper (Faculty faculty, AppUserDetailByIdDto appUserDetailByIdDto){
        FacultyDetailByIdDto facultyDetailByIdDto = new FacultyDetailByIdDto();
        facultyDetailByIdDto.setUserName(appUserDetailByIdDto.getUserName());
        facultyDetailByIdDto.setUserEmail(appUserDetailByIdDto.getUserEmail());
        facultyDetailByIdDto.setPhoneNumber(appUserDetailByIdDto.getPhoneNumber());
        facultyDetailByIdDto.setFacultyGender(faculty.getFacultyGender());
        facultyDetailByIdDto.setFacultyYearOfExperience(faculty.getFacultyYearOfExperience());
        facultyDetailByIdDto.setFacultyAddress(faculty.getFacultyAddress());
        facultyDetailByIdDto.setFacultyRating(faculty.getFacultyRating());
        return  facultyDetailByIdDto;
    }

}
