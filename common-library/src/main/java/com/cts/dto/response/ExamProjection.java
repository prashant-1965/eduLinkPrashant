package com.cts.dto.response;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ExamProjection {
    private String examName;
    private LocalDateTime examLocalDateTime;
    private String examStatus;
    private int candidates;
}
