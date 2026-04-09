package com.cts.engagement_service.application.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "faculty-service")
public interface FacultyFeign {
    @GetMapping("/faculty/checkFacultyExistByFacultyId/{facultyId}")
    void checkFacultyExistByFacultyId(@PathVariable Long facultyId);
}
