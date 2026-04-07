package com.cts.course_service.application.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "enrollment-service")
public interface CourseEnrollmentFeign {
    @PostMapping("/faculty-course-assignment/assign/{facultyId}/{courseId}")
    void assignCourseToFaculty(@PathVariable("facultyId") Long facultyId, @PathVariable("courseId") Long courseId);

    @GetMapping("/faculty-course-assignment/findCourseListByFacultyId/{facultyId}")
    List<Long> getCoursesListByFacultyId(@PathVariable Long facultyId);

    @GetMapping("/faculty-course-assignment/getCourseCountByFacultyId/{facultyId}")
    int getFacultyCourseCount(@PathVariable Long facultyId);

    @GetMapping("/faculty-course-assignment/findFacultyIdByCourseId/{courseId}")
    Long findFacultyIdByCourseId(@PathVariable Long courseId);

    @PostMapping("/student-course-assignment/assign/{studentId}/{courseId}")
    void assignCourseToStudent(@PathVariable("studentId") Long studentId, @PathVariable("courseId") Long courseId);

    @GetMapping("/student-course-assignment/findCourseListBystudentId/{studentId}")
    List<Long> getCoursesListByStudentId(@PathVariable Long studentId);

}
