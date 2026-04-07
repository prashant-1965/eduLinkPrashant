package com.cts.course_service.application.feign;

import com.cts.dto.response.FacultyDetailProjection;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "faculty-service")
public interface FacultyFeign {

    @GetMapping("/faculty/checkFacultyByFacultyId/{facultyId}")
    void checkFacultyByFacultyId(@PathVariable Long facultyId);

    @GetMapping("/faculty/getFacultyDetailsByFacultyId/{facultyId}")
    FacultyDetailProjection getFacultyDetailsByFacultyId(@PathVariable Long facultyId);
}
