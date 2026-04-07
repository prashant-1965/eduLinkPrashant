package com.cts.enrollment_service.application.controller;

import com.cts.enrollment_service.application.service.IFacultyCourseEnrollmentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/faculty-course-assignment")
public class FacultyCourseAssignmentController {

    private final IFacultyCourseEnrollmentService facultyCourseEnrollmentService;


    @PostMapping("/assign/{facultyId}/{courseId}")
    public void assignCourseToFaculty(@PathVariable Long facultyId, @PathVariable Long courseId) {
        log.info("Received request to assign course with ID {} to faculty with ID {}", courseId, facultyId);
        facultyCourseEnrollmentService.assignCourseToFaculty(facultyId, courseId);
        log.info("Successfully assigned course with ID {} to faculty with ID {}", courseId, facultyId);
    }

    @GetMapping("/findCourseListByFacultyId/{facultyId}")
    public List<Long> getCoursesListByFacultyId(@PathVariable Long facultyId) {
        log.info("Received request to get courses assigned to faculty with ID {}", facultyId);
        List<Long> courses = facultyCourseEnrollmentService.getCoursesListByFacultyId(facultyId);
        log.info("Successfully retrieved list of courses assigned to faculty with ID {}", facultyId);
        return courses;
    }

    @GetMapping("/getCourseCountByFacultyId/{facultyId}")
    public int getFacultyCourseCount(@PathVariable Long facultyId) {
        log.info("Received request to get faculty assigned to course with ID {}", facultyId);
        int courseCount = facultyCourseEnrollmentService.getFacultyCourseCount(facultyId);
        log.info("Successfully retrieved faculty count assigned to course with ID {}", facultyId);
        return courseCount;
    }

    @GetMapping("/findFacultyIdByCourseId/{courseId}")
    public Long findFacultyIdByCourseId(@PathVariable Long courseId) {
        log.info("Received request to find faculty assigned to course with ID {}", courseId);
        Long facultyId = facultyCourseEnrollmentService.findFacultyIdByCourseId(courseId);
        log.info("Successfully retrieved faculty assigned to course with ID {}", courseId);
        return facultyId;
    }
}
