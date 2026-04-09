package com.cts.engagement_service.application.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "enrollment-service")
public interface StudentCourseEnrollmentFeign {

    @GetMapping("/student-course-assignment/checkEnrollment/{studentId}/{courseId}")
    void checkStudentExistInCourse(@PathVariable("studentId") Long studentId, @PathVariable("courseId") Long courseId);

    @GetMapping("/student-course-assignment/findCourseListBystudentId/{studentId}")
    List<Long> getCoursesListByStudentId(@PathVariable Long studentId);
}
