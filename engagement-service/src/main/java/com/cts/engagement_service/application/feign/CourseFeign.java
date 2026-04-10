package com.cts.engagement_service.application.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course-service")
public interface CourseFeign {
    @GetMapping("/course/checkCourseExistByCourseId/{courseId}")
    void checkCourseExistByCourseId(@Valid @PathVariable Long courseId);
    @GetMapping("/course/findCourseTitleByCourseId/{courseId}")
    String findCourseTitleByCourseId(@Valid @PathVariable Long courseId);
}
