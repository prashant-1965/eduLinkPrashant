package com.cts.academic_service.application.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Setter
@Entity

public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long examId;
    private LocalDateTime examLocalDateTime;
    private String examName;
    private String examStatus;
    private int candidates;
    private Long courseId;

    @OneToMany(mappedBy = "exam")
    private List<Grade> gradeList;
}
