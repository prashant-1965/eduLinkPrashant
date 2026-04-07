package com.cts.engagement_service.application.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String notificationMessage;
    private Long appUserId;
}

