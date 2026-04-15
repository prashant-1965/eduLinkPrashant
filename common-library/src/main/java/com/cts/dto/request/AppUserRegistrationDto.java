package com.cts.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserRegistrationDto {

    @NotBlank(message = "Name is required")
    private String userName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String userEmail;

    @NotNull(message = "Phone number is required")
    @Min(value = 1000000000L, message = "Phone number must be 10 digits")
    @Max(value = 9999999999L, message = "Phone number must be 10 digits")
    private Long phoneNumber;

    @NotBlank(message = "Role is required")
    private String role;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one uppercase, one lowercase, one digit, and one special character")
    private String password;

    public static <T extends IUserRegistration> AppUserRegistrationDto from(T userRegistration, String role) {
        AppUserRegistrationDto dto = new AppUserRegistrationDto();
        dto.setUserName(userRegistration.getUserName());
        dto.setUserEmail(userRegistration.getUserEmail());
        dto.setPhoneNumber(userRegistration.getPhoneNumber());
        dto.setPassword(userRegistration.getPassword());
        dto.setRole(role);
        return dto;
    }
}
