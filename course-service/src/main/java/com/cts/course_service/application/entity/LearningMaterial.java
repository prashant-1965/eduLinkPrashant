package com.cts.course_service.application.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class LearningMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String learningMaterialTitle;
    private String learningMaterialFile;
    private LocalDateTime learningMaterialUploadedDate;
    private String learningMaterialStatus; // UPLOADED, pending
    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

}
