package com.cts.engagement_service.application.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class PerformanceMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private Long metricId;
    private Long studentId;
    private Long courseId;

    private double score;
    private LocalDate localDate;
    private  String status;
}
