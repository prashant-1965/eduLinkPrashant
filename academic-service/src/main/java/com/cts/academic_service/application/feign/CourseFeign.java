package com.cts.academic_service.application.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course-service")
public interface CourseFeign {
    @GetMapping("/course/checkCourseExistByCourseId/{courseId}")
    void checkCourseExistByCourseId(@Valid @PathVariable Long courseId);
}
