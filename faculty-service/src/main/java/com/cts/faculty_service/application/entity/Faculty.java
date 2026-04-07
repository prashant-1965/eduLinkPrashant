package com.cts.faculty_service.application.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private Long facultyId;
    private String facultyGender;
    private String facultyAddress;
    private int facultyYearOfExperience;
    private double facultyRating;
    private long totalFacultyRatingCount;
    private Long appUserId;
}

