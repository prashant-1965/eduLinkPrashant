package com.cts.academic_service.application.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long assignmentId;

    private String assignmentTitle;
    private String assignmentDescription;
    private LocalDateTime assignmentCreatedDate;
    private LocalDateTime assignmentDueDate;
    private String assignmentStatus;
    private int totalMarks;
    private Long courseId;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL)
    private List<AssignmentStatus> assignmentStatusList;
}

