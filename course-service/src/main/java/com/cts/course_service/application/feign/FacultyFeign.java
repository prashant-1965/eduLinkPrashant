package com.cts.course_service.application.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "faculty-service")
public interface FacultyFeign {
    @GetMapping("/faculty/getFacultyByFacultyId/{facultyId}")
    void getFacultyByFacultyId(@PathVariable Long facultyId);
}
