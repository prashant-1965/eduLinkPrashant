package com.cts.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CourseEnrollmentDto {
    @NotNull(message = "Course ID is required for enrollment")
    private Long courseId;

    @NotNull(message = "Student ID is required for enrollment")
    private Long studentId;
}
