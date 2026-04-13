package com.cts.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttendanceRegistrationDto {
    @NotNull(message = "Student ID is required to mark attendance")
    private Long studentId;

    @NotNull(message = "Course ID is required")
    private Long courseId;
}
