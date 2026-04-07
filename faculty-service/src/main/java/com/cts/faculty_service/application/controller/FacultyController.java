package com.cts.faculty_service.application.controller;

import com.cts.dto.request.FacultyRegistrationDto;
import com.cts.dto.response.FacultyDetailProjection;
import com.cts.faculty_service.application.service.IFacultyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/faculty")
@AllArgsConstructor
@Slf4j
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
        log.info("Request has been initiated to get Faculty details by facultyId {}", facultyId);
        return facultyService.getFacultyDetailsByFacultyId(facultyId);
    }
}
