package com.cts.course_service.application.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "enrollment-service")
public interface FacultyCourseEnrollmentFeign {
    @PostMapping("/faculty-course-assignment/assign/{facultyId}/{courseId}")
    void assignCourseToFaculty(@PathVariable("facultyId") Long facultyId, @PathVariable("courseId") Long courseId);
}
