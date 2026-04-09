package com.cts.faculty_service.application.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.cts.dto.response.CourseProjection;

import java.util.List;

@FeignClient(name = "course-service")
public interface CourseFeign {
    @GetMapping("/course/getCoursesByFacultyId/{facultyId}")
    ResponseEntity<List<CourseProjection>> getCoursesByFaculty(@Valid @PathVariable Long facultyId);
}
