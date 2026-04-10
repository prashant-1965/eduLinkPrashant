package com.cts.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class LearningMaterialRegistrationDto {
    @NotBlank(message = "Material title is required")
    @Size(min = 3, max = 150, message = "Title must be between 3 and 150 characters")
    private String learningMaterialTitle;

    @NotNull(message = "Please select a file to upload")
    private MultipartFile learningMaterialFile;

    @NotNull(message = "Course ID is required to link this material")
    private Long courseId;
}
