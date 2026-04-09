package com.cts.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ExamCreationRequestDto {
    @NotBlank(message = "Exam name is required")
    @Size(max = 100, message = "Exam name cannot exceed 100 characters")
    private String examName;

    @NotNull(message = "Course ID is required to link the exam")
    private Long courseId;

    @NotBlank(message = "Status is required (e.g., UPCOMING, ACTIVE)")
    @Pattern(regexp = "^(UPCOMING|ACTIVE|COMPLETED)$",
            message = "Status must be UPCOMING, ACTIVE, or COMPLETED")
    private String status;

    @Min(value = 1, message = "Exam must have at least 1 candidate seat")
    private int candidates;
}
