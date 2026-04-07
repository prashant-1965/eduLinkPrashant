package com.cts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FacultyDetailProjection {
    private String facultyName;
    private double facultyRating;
    private int facultyYearOfExperience;
}
