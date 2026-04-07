package com.cts.globalexception;


import com.cts.classexception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.SecurityException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(FacultyCourseEnrollmentException.class)
    public ResponseEntity<String> facultyCourseEnrollmentExceptionHandler(FacultyCourseEnrollmentException fe){
        return ResponseEntity.status(fe.getHttpStatus()).body(fe.getMessage());
    }

    @ExceptionHandler(CourseException.class)
    public ResponseEntity<String> courseExceptionHandler(CourseException c){
        return ResponseEntity.status(c.getHttpStatus()).body(c.getMessage());
    }

    @ExceptionHandler(AppUserException.class)
    public ResponseEntity<String> appUserExceptionHandler(AppUserException a){
        return ResponseEntity.status(a.getHttpStatus()).body(a.getMessage());
    }

    @ExceptionHandler(StudentException.class)
    public ResponseEntity<String> studentExceptionHandler(StudentException s){
        return ResponseEntity.status(s.getHttpStatus()).body(s.getMessage());
    }

    @ExceptionHandler(FacultyException.class)
    public ResponseEntity<String> facultyExceptionHandler(FacultyException f){
        return ResponseEntity.status(f.getHttpStatus()).body(f.getMessage());
    }

    @ExceptionHandler(LearningMaterialException.class)
    public ResponseEntity<String> learningMaterialExceptionHandler(LearningMaterialException l) {
        return ResponseEntity.status(l.getHttpStatus()).body(l.getMessage());
    }
    @ExceptionHandler(FileException.class)
    public ResponseEntity<String> fileExceptionHandler(FileException f) {
        return ResponseEntity.status(f.getHttpStatus()).body(f.getMessage() + " " + f.getCause());
    }
    @ExceptionHandler(ExamException.class)
    public ResponseEntity<String> handleExamException(ExamException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(com.cts.classexception.SecurityException ex) {
        return new ResponseEntity<>(ex.getMessage(),ex.getHttpStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> accessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        log.error("Validation failed: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }
}

