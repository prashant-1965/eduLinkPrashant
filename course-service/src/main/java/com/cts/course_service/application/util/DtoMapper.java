package com.cts.course_service.application.util;

import com.cts.course_service.application.entity.Course;
import com.cts.dto.request.CourseRegistrationDto;
import com.cts.util.UIDGeneratorUtils;

public class DtoMapper {
    public static Course courseDtoSeparator(CourseRegistrationDto courseRegistrationDto){
        Course course = new Course();
        course.setCourseTitle(courseRegistrationDto.getCourseTitle());
        course.setCourseSubject(courseRegistrationDto.getCourseSubject());
        course.setCourseCredit(courseRegistrationDto.getCourseCredit());
        course.setCourseRating(0.0);
        course.setTotalCourseRatingCount(0L);
        course.setCourseGradeLevel(courseRegistrationDto.getCourseGradeLevel());
        Long courseId = UIDGeneratorUtils.uidGenerator();
        course.setCourseId(courseId);
        return course;
    }
}
