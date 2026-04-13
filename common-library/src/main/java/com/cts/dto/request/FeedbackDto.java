package com.cts.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class FeedbackDto {
    @NotNull(message = "User ID is required")
    private Long appUserRoleId;

    @NotBlank(message = "Reviewer type is required")
    @Pattern(regexp = "^(STUDENT|FACULTY)$",
            message = "Reviewer type must be either 'STUDENT' or 'FACULTY'")
    private String reviewerType;

    @DecimalMin(value = "1.0", message = "Rating must be at least 1.0")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    private double rating;

    @NotBlank(message = "Comment cannot be empty")
    @Size(min = 10, max = 500, message = "Comment must be between 10 and 500 characters")
    private String comment;
}
