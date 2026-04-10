package com.cts.student_service.application.service;

import com.cts.dto.request.StudentRegistrationDto;
import com.cts.student_service.application.entity.Student;
import com.cts.classexception.StudentException;
import com.cts.student_service.application.feign.AppUserFeign;
import com.cts.student_service.application.repository.StudentRepository;
import com.cts.student_service.application.util.DtoMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class StudentServiceImpl implements IStudentService{

    private final StudentRepository studentRepository;
    private final AppUserFeign appUserFeign;

    @Override
    @Transactional
    @Retry(name = "registerStudent", fallbackMethod = "registerFallback")
    @CircuitBreaker(name = "registerStudent", fallbackMethod = "registerFallback")
    public String registerStudent(StudentRegistrationDto studentRegistrationDto) throws StudentException {
        log.info("Initiating student registration for user: {}", studentRegistrationDto.getUserEmail());
        log.debug("Extracting student and user entities from DTO");
        Student student = DtoMapper.studentDtoSeparator(studentRegistrationDto);
        ResponseEntity<Long> appUserId = appUserFeign.appUserRegistration(studentRegistrationDto);
        student.setAppUserId(appUserId.getBody());
        log.error("Attempting to register AppUser and save Student entity");
        studentRepository.save(student);
        log.info("Successfully registered student. Assigned Student ID: {}", student.getStudentId());
        return "Thanks for Registration, Your User Id is: "+student.getStudentId();
    }

    @Override
    public void checkStudentExistByStudentId(Long studentId) throws StudentException {
        log.info("Checking existence of student with ID: {}", studentId);
        boolean exists = studentRepository.existsByStudentId(studentId);
        if (!exists) {
            log.error("Enrollment failed: Student ID {} not found", studentId);
            throw new StudentException("Student with ID " + studentId + " does not exist.", HttpStatus.NOT_FOUND);
        }
        log.info("Student with ID {} exists", studentId);
    }

    @Override
    public String getStudentNameByStudentId(Long studentId) {
        Long appUserId = studentRepository.findAppUserIdByStudentId(studentId);
        log.info("Fetching student name for Student ID: {} with App User ID: {}", studentId, appUserId);
        String studentName = appUserFeign.findAppUserNameByAppUserId(appUserId);
        log.info("Retrieved student name: {} for Student ID: {}", studentName, studentId);
        return studentName;
    }

    public String registerFallback(StudentRegistrationDto studentRegistrationDto, Throwable t) {
        log.error("Fallback triggered for user: {}. Reason: {}",
                studentRegistrationDto.getUserEmail(), t.getMessage());
        return "Registration service is currently experiencing high traffic. " +
                "Please try again in a few moments. Error: " + t.getLocalizedMessage();
    }
}
