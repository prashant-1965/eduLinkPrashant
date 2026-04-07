package com.cts.student_service.application.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private Long studentId;

    private LocalDate studentDOB;
    private String studentGender;
    private String studentAddress;
    private LocalDateTime studentEnrollmentDateTime;
    private Long appUserId;
}

