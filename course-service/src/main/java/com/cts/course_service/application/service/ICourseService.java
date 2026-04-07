package com.cts.course_service.application.service;

import com.cts.dto.request.CourseRegistrationDto;

public interface ICourseService {
    String registerCourse(CourseRegistrationDto courseRegistrationDto);
}
