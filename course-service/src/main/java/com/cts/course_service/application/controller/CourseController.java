package com.cts.course_service.application.controller;

import com.cts.course_service.application.projection.CourseDetailProjection;
import com.cts.dto.response.CourseProjection;
import com.cts.course_service.application.service.ICourseService;
import com.cts.dto.request.CourseEnrollmentDto;
import com.cts.dto.request.CourseRegistrationDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.cts.dto.response.CourseDetailByIdProjection;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/course")
@AllArgsConstructor
@Slf4j

public class CourseController {

    private final ICourseService iCourseService;

    @PreAuthorize("hasRole('FACULTY')")
    @PostMapping("/register")
    public ResponseEntity<String> registerCourse(@Valid @RequestBody CourseRegistrationDto courseRegistrationDto){
        log.info("{} request for a new course registration",courseRegistrationDto.getFacultyId());
        return  ResponseEntity.status(200).body(iCourseService.registerCourse(courseRegistrationDto));
    }

    @GetMapping("/checkCourseExistByCourseId/{courseId}")
    public void checkCourseExistByCourseId(@Valid @PathVariable Long courseId){
        iCourseService.checkCourseExistByCourseId(courseId);
    }

    @GetMapping("/findCourseTitleByCourseId/{courseId}")
    public String findCourseTitleByCourseId(@Valid @PathVariable Long courseId){
        return iCourseService.findCourseTitleByCourseId(courseId);
    }

    @PreAuthorize("hasAnyRole('FACULTY', 'STUDENT')")
    @GetMapping("/findCourseDetailsById/{courseId}")
    public ResponseEntity<CourseDetailByIdProjection> findCourseById(@Valid @PathVariable Long courseId) {
        log.info("User requested for details of courseId: {} ", courseId);
        return ResponseEntity.status(200).body(iCourseService.findCourseDetailsById(courseId));
    }

    @PreAuthorize("hasRole('FACULTY')")
    @PutMapping("/update/{courseId}")
    public ResponseEntity<String> updateCourse(@Valid @PathVariable Long courseId, @RequestBody CourseRegistrationDto courseRegistrationDto) {
        log.info("Received request to update course with ID: {}", courseId);
        String response = iCourseService.updateCourse(courseId, courseRegistrationDto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('FACULTY')")
    @PatchMapping("/patch/{courseId}")
    public ResponseEntity<String> patchCourse(@Valid @PathVariable Long courseId, @RequestBody Map<String, Object> updates) {
        log.info("Received patch request for courseId: {}", courseId);
        String response = iCourseService.patchCourse(courseId, updates);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('FACULTY')")
    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<String> deleteCourse(@Valid @PathVariable Long courseId) {
        log.info("Received request to delete course with ID: {}", courseId);
        String response = iCourseService.deleteCourse(courseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/findAllAvailableCourse")
    public ResponseEntity<List<CourseProjection>> findALlAvailableCourse(){
        log.info("User has called the endpoint successFully to fetch all available courses");
        return ResponseEntity.status(200).body(iCourseService.findAllAvailableCourse());
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/allCourseListByStudentId/{studentId}")
    public ResponseEntity<List<CourseDetailProjection>> findCourseListByStudentId(@Valid @PathVariable Long studentId){
        log.info("Received GET request: Fetching courses for studentId: {}", studentId);
        return ResponseEntity.status(200).body(iCourseService.findCourseListByStudentId(studentId));
    }

    @PreAuthorize("hasAnyRole('FACULTY', 'STUDENT')")
    @GetMapping("/getCoursesByFacultyId/{facultyId}")
    public ResponseEntity<List<CourseProjection>> getCoursesByFaculty(@Valid @PathVariable Long facultyId) {
        return ResponseEntity.status(200).body(iCourseService.getCoursesByFaculty(facultyId));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/enrollmentRequest")
    public ResponseEntity<String> courseEnrollmentRequest(@Valid @RequestBody CourseEnrollmentDto courseEnrollmentDto){
        log.info("Received PATCH request: Enrollment attempt for Student: {} on Course: {}",courseEnrollmentDto.getStudentId(), courseEnrollmentDto.getCourseId());
        return ResponseEntity.status(200).body(iCourseService.courseEnrollmentRequest(courseEnrollmentDto));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PatchMapping("/updateRating/{courseId}/{newCourseRating}")
    public ResponseEntity<String> updateCourseRating(@Valid @PathVariable Long courseId, @PathVariable double newCourseRating) {
        log.info("Received PATCH request: Updating rating for courseId: {} to {}", courseId, newCourseRating);
        return ResponseEntity.status(200).body(iCourseService.updateCourseRating(courseId, newCourseRating));
    }

    @GetMapping("/courseCount/{facultyId}")
    public Map<String, Integer> getFacultyCourseCount(@Valid @PathVariable Long facultyId) {
        int count = iCourseService.getFacultyCourseCount(facultyId);
        return Map.of("My Courses", count);
    }
}
