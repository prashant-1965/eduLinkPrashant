package com.cts.enrollment_service.application.controller;

import com.cts.enrollment_service.application.service.IFacultyCourseEnrollmentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/faculty-course-assignment")
@Slf4j
@AllArgsConstructor
public class FacultyCourseAssignmentController {

    private final IFacultyCourseEnrollmentService facultyCourseEnrollmentService;


    @PostMapping("/assign/{facultyId}/{courseId}")
    public void assignCourseToFaculty(@PathVariable Long facultyId, @PathVariable Long courseId) {
        log.info("Received request to assign course with ID {} to faculty with ID {}", courseId, facultyId);
        facultyCourseEnrollmentService.assignCourseToFaculty(facultyId, courseId);
        log.info("Successfully assigned course with ID {} to faculty with ID {}", courseId, facultyId);
    }
}
