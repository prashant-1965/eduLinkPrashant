package com.cts.engagement_service.application.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double rating;
    private String message;
    private String appUserName;
    private Long appUserRoleId;
}
