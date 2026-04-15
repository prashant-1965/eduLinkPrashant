package com.cts.dto.request;


import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentRegistrationDto implements IUserRegistration {

    @NotBlank(message = "Name is required")
    private String userName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false)
    private String userEmail;

    @NotNull(message = "Phone number is required")
    @Min(value = 1000000000L, message = "Phone number must be 10 digits")
    @Max(value = 9999999999L, message = "Phone number must be 10 digits")
    @Column(unique = true, nullable = false)
    private Long phoneNumber;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Birth date must be in the past")
    @Column(nullable = false)
    private LocalDate studentDOB;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(Male|Female|Other|Prefer not to say)$")
    @Column(nullable = false)
    private String studentGender;

    @NotBlank(message = "Address is required")
    @Column(nullable = false)
    private String studentAddress;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one uppercase, one lowercase, one digit, and one special character")
    private String password;
}
