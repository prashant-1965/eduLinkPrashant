package com.cts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ExamProjection {
    private String examName;
    private LocalDateTime examLocalDateTime;
    private String examStatus;
    private int candidates;
}
