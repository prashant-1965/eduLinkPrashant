package com.cts.iam_service.application.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String userEmail;
    private Long phoneNumber;
    private String userPassword;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}

