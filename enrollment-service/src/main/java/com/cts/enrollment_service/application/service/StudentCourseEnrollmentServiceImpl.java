package com.cts.enrollment_service.application.service;


import com.cts.classexception.StudentCourseEnrollmentException;
import com.cts.enrollment_service.application.entity.StudentCourseEnrollment;
import com.cts.enrollment_service.application.repository.StudentCourseEnrollmentRepository;
import com.cts.enrollment_service.application.util.DtoMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class StudentCourseEnrollmentServiceImpl implements IStudentCourseEnrollmentService{

    private final StudentCourseEnrollmentRepository studentCourseEnrollmentRepository;

    @Override
    public void assignCourseToStudent(Long studentId, Long courseId) throws StudentCourseEnrollmentException {
        log.info("Received request to assign course with ID {} to student with ID {}", courseId, studentId);
        boolean isAlreadyEnrolled = studentCourseEnrollmentRepository.isAlreadyEnrolled(studentId, courseId);
        if (isAlreadyEnrolled) {
            log.warn("Student with ID {} is already enrolled in course with ID {}", studentId, courseId);
            throw new StudentCourseEnrollmentException("Student is already enrolled in this course.", HttpStatus.CONFLICT);
        }
        StudentCourseEnrollment studentCourseEnrollment = DtoMapper.mapToStudentCourseAssignment(courseId,studentId);
        studentCourseEnrollmentRepository.save(studentCourseEnrollment);
        log.info("Successfully assigned course with ID {} to student with ID {}", courseId, studentId);

    }

    @Override
    public List<Long> getEnrolledCourseIdsByStudentId(Long studentId) throws StudentCourseEnrollmentException {
        List<Long> enrolledCourseIds = studentCourseEnrollmentRepository.findCourseIdsByStudentId(studentId);
        if(enrolledCourseIds.isEmpty()){
            log.info("No enrolled courses found for student with ID {}", studentId);
            throw new StudentCourseEnrollmentException("No enrolled courses found for the student.", HttpStatus.NOT_FOUND);
        }
        log.info("Retrieved enrolled course IDs for student with ID {}: {}", studentId, enrolledCourseIds);
        return enrolledCourseIds;
    }
}
