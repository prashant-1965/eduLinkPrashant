package com.cts.course_service.application.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CourseDetailProjection {
    private String courseTitle;
    private String courseGradeLevel;
    private double courseRating;
}
