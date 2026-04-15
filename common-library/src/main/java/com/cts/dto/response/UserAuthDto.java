package com.cts.dto.response;

import lombok.Data;

@Data
public class UserAuthDto {
    private Long id;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String roleName;
}
