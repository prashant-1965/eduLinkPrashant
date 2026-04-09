package com.cts.course_service.application.service;

import com.cts.dto.response.CourseDetailByIdProjection;
import com.cts.course_service.application.projection.CourseDetailProjection;
import com.cts.dto.response.CourseProjection;
import com.cts.dto.request.CourseEnrollmentDto;
import com.cts.dto.request.CourseRegistrationDto;

import java.util.List;
import java.util.Map;

public interface ICourseService {
    String registerCourse(CourseRegistrationDto courseRegistrationDto);
    List<CourseProjection> findAllAvailableCourse();
    String updateCourse(Long courseId, CourseRegistrationDto courseRegistrationDto);
    String patchCourse(Long courseId, Map<String, Object> updates);
    String deleteCourse(Long courseId);
    List<CourseProjection> getCoursesByFaculty(Long facultyId);
    public int getFacultyCourseCount(Long facultyId );
    CourseDetailByIdProjection findCourseDetailsById(Long courseId);
    String courseEnrollmentRequest(CourseEnrollmentDto courseEnrollmentDto);
    String updateCourseRating(Long courseId, double newCourseRating);
    List<CourseDetailProjection> findCourseListByStudentId(Long studentId);

}
