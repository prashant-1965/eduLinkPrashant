package com.cts.engagement_service.application.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "course-service")
public interface CourseFeign {
    @GetMapping("/course/checkCourseExistByCourseId/{courseId}")
    void checkCourseExistByCourseId(Long courseId);
    @GetMapping("/course/findCourseTitleByCourseId/{courseId}")
    String findCourseTitleByCourseId(Long courseId);
}
