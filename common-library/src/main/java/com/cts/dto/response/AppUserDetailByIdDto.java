package com.cts.dto.response;


import lombok.Data;

@Data
public class AppUserDetailByIdDto {
    private String userName;
    private String userEmail;
    private Long phoneNumber;
}
