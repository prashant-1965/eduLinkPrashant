package com.cts.student_service.application.controller;

import com.cts.dto.request.StudentRegistrationDto;
import com.cts.student_service.application.service.IStudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
@AllArgsConstructor
@Slf4j
public class StudentController {

    private final IStudentService iStudentService;

    @PostMapping("/register")
    public ResponseEntity<String> studentRegistration(@Valid @RequestBody StudentRegistrationDto studentRegistrationDto){
        log.info("Student's registration request has been initiated successFully by {}",studentRegistrationDto.getUserName());
        return ResponseEntity.status(200).body(iStudentService.registerStudent(studentRegistrationDto));
    }

    @GetMapping("/checkStudentExistByStudentId/{studentId}")
    public void checkStudentExistByStudentId(@PathVariable Long studentId){
        iStudentService.checkStudentExistByStudentId(studentId);
    }
}
