package com.cts.engagement_service.application.controller;

import com.cts.dto.request.AttendanceRegistrationDto;
import com.cts.dto.response.CourseAttendanceProjection;
import com.cts.engagement_service.application.service.IAttendanceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/attendance")
public class AttendanceController {

    private final IAttendanceService attendanceService;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/register")
    public ResponseEntity<String> registerAttendanceByStudentId(@Valid @RequestBody AttendanceRegistrationDto attendanceRegistrationDto){
        log.info("Attendance registration request initiated for Student ID: {} in Course ID: {}", attendanceRegistrationDto.getStudentId(), attendanceRegistrationDto.getCourseId());
        return ResponseEntity.status(200).body(attendanceService.registerAttendanceByStudentId(attendanceRegistrationDto));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/attendanceDetailsByStudentId/{studentId}")
    public ResponseEntity<List<CourseAttendanceProjection>> findAttendanceDetailsByStudentId(@Valid @PathVariable Long studentId){
        log.info("REST request to get attendance details for Student ID: {}", studentId);
        return ResponseEntity.status(200).body(attendanceService.findAttendanceByCourse(studentId));
    }
}
