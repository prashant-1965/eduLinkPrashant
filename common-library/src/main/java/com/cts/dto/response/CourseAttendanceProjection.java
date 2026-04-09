package com.cts.dto.response;

import lombok.Data;

@Data
public class CourseAttendanceProjection {
    private Long courseId;
    private String courseTitle;
    private double attendancePercentage;
}
