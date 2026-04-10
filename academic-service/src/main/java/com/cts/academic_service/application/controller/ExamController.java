package com.cts.academic_service.application.controller;


import com.cts.academic_service.application.service.IExamService;
import com.cts.dto.request.ExamCreationRequestDto;
import com.cts.dto.response.ExamProjection;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/exam")
@Slf4j
public class ExamController {

    private final IExamService examService;

    @PostMapping("/register")
    public ResponseEntity<String> createExam(@Valid @RequestBody ExamCreationRequestDto request) {
        log.info("Received request to create exam: {}", request.getExamName());
        return ResponseEntity.status(200).body(examService.createExam(request));
    }

    @DeleteMapping("/delete/{examId}")
    public ResponseEntity<String> deleteExam(@Valid @PathVariable Long examId) {
        log.info("Delete operation for examId: {} has been initiated successfully", examId);
        return ResponseEntity.status(200).body(examService.deleteExam(examId));
    }

    @GetMapping("/allExams")
    public ResponseEntity<List<ExamProjection>> getAllExams(){
        log.info("Controller: Request received to fetch all exam projections");
        return ResponseEntity.status(200).body(examService.findAllExams());
    }

}
