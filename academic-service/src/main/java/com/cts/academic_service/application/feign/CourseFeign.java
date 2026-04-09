package com.cts.academic_service.application.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "course-service")
public interface CourseFeign {
    @GetMapping("/course/checkCourseExistByCourseId/{courseId}")
    void checkCourseExistByCourseId(Long courseId);
}
