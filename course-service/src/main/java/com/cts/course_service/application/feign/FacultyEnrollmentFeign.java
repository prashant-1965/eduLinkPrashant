package com.cts.course_service.application.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "faculty-enrollment-service")
public interface FacultyEnrollmentFeign {
    @PostMapping("/assign/{facultyId}/{courseId}")
    void assignCourseToFaculty( Long facultyId, Long courseId);
}
