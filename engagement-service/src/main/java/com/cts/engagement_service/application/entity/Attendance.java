package com.cts.engagement_service.application.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long attendanceId;
    private Long studentId;
    private Long courseId;

    private LocalDateTime localDateTime;
}
