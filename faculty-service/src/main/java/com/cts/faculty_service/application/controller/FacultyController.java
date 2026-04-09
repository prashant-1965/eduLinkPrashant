package com.cts.faculty_service.application.controller;

import com.cts.dto.request.FacultyRegistrationDto;
import com.cts.dto.response.FacultyDetailProjection;
import com.cts.faculty_service.application.service.IFacultyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cts.dto.response.CourseProjection;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/faculty")
public class FacultyController {
    private final IFacultyService facultyService;

    @PostMapping("/register")
    public ResponseEntity<String> registerFaculty(@Valid @RequestBody FacultyRegistrationDto facultyRegistrationDto){
        log.info("{} has initiated the registration as a Faculty",facultyRegistrationDto.getUserEmail());
        return ResponseEntity.status(200).body(facultyService.registerFaculty(facultyRegistrationDto));
    }

    @GetMapping("/checkFacultyByFacultyId/{facultyId}")
    public void checkFacultyByFacultyId(@PathVariable Long facultyId){
        log.info("Request has been initiated to get Faculty details by facultyId {}",facultyId);
        facultyService.checkFacultyByFacultyId(facultyId);
    }

    @GetMapping("/getFacultyDetailsByFacultyId/{facultyId}")
    public FacultyDetailProjection getFacultyDetailsByFacultyId(@PathVariable Long facultyId) {
        log.info("Request has been initiated to get Faculty details by facultyID {}", facultyId);
        return facultyService.getFacultyDetailsByFacultyId(facultyId);
    }

    @GetMapping("/getFacultyCourses/{facultyId}")
    public ResponseEntity<List<CourseProjection>> getFacultyCourses(@Valid @PathVariable Long facultyId) {
        log.info("Received request to get courses for faculty with ID: {}", facultyId);
        List<CourseProjection> courses = facultyService.getFacultyCourses(facultyId);
        return ResponseEntity.status(200).body(courses);
    }

    @DeleteMapping("/delete/{facultyId}")
    public ResponseEntity<String> deleteFaculty(@Valid @PathVariable Long facultyId) {
        log.info("Received request to delete faculty with ID: {}", facultyId);
        String response = facultyService.deleteFaculty(facultyId);
        return ResponseEntity.status(200).body(response);
    }

    @PatchMapping("/updateRating/{facultyId}/{newFacultyRating}")
    public ResponseEntity<String> updateFacultyRating(@Valid @PathVariable Long facultyId, @PathVariable double newFacultyRating){
        return ResponseEntity.status(200).body(facultyService.updateFacultyRating(facultyId,newFacultyRating));
    }

//    @GetMapping("/upcoming/{facultyId}")
//    public ResponseEntity<List<Exam>> getupComingExams(@Valid @PathVariable Long facultyId) {
//        // This now matches the return type of your service/repository
//        return ResponseEntity.ok(facultyService.getupComingExams(facultyId));
//    }
//
//    @GetMapping("/upComingCount/{facultyId}")
//    public Map<String,Integer> getupComingExamsCount(@Valid @PathVariable Long facultyId){
//        int count = facultyService.getupComingExamsCount(facultyId);
//        return Map.of("upComing Exams", count);
//    }
}
