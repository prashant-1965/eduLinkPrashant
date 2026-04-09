package com.cts.faculty_service.application.service;

import com.cts.dto.request.FacultyRegistrationDto;
import com.cts.dto.response.FacultyDetailProjection;
import com.cts.dto.response.CourseProjection;

import java.util.List;

public interface IFacultyService {
    String registerFaculty(FacultyRegistrationDto facultyRegistrationDto);
    void checkFacultyByFacultyId(Long facultyId);
    FacultyDetailProjection getFacultyDetailsByFacultyId(Long facultyId);
    String updateFacultyRating(Long facultyId, double newFacultyRating);
    List<CourseProjection> getFacultyCourses(Long facultyId);
    public String deleteFaculty(Long facultyId);
//    List<Exam> getupComingExams(Long facultyId );
//    public  int getupComingExamsCount(Long facultyId);

}
