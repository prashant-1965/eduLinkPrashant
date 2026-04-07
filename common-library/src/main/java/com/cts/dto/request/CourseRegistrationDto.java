package com.cts.dto.request;


import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseRegistrationDto {

    @NotBlank(message = "Course title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String courseTitle;

    @NotBlank(message = "Subject is required")

    private String courseSubject;

    @NotBlank(message = "Grade level is required")
    @Pattern(regexp = "^(9|10|11|12|K|Undergrad)$", message = "Invalid grade level")
    private String courseGradeLevel;

    @Min(value = 1, message = "Course must be worth at least 1 credit")
    @Max(value = 5, message = "Course cannot exceed 10 credits")
    private int courseCredit;


    @NotNull(message = "Faculty ID must be provided")
    @Positive(message = "Invalid Faculty ID")
    @Column(unique = true ,nullable=false)
    private Long facultyId;
}


