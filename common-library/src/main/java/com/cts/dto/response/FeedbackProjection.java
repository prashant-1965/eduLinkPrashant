package com.cts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackProjection {
    private String appUserName;
    private String message;
    private Double rating; // Changed from double to Double
}
