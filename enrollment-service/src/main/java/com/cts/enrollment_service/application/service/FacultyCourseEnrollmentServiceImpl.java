package com.cts.enrollment_service.application.service;


import com.cts.classexception.CourseException;
import com.cts.enrollment_service.application.entity.FacultyCourseAssignment;
import com.cts.enrollment_service.application.repository.FacultyCourseEnrollmentRepository;
import com.cts.classexception.FacultyCourseEnrollmentException;
import com.cts.enrollment_service.application.util.DtoMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class FacultyCourseEnrollmentServiceImpl implements IFacultyCourseEnrollmentService{

    private final FacultyCourseEnrollmentRepository facultyCourseEnrollmentRepository;

    @Override
    @Transactional
    @CircuitBreaker(name = "enrollmentService", fallbackMethod = "fallbackAssignCourse")
    @Retry(name = "enrollmentService")
    public void assignCourseToFaculty(Long facultyId, Long courseId)throws FacultyCourseEnrollmentException {
        boolean isAlreadyAssigned = facultyCourseEnrollmentRepository.existsByFacultyIdAndCourseId(facultyId, courseId);
        if (isAlreadyAssigned) {
            log.warn("Course with ID {} is already assigned to faculty with ID {}", courseId, facultyId);
            throw new FacultyCourseEnrollmentException("Course with ID " + courseId + " is already assigned to faculty with ID " + facultyId, HttpStatus.CONFLICT);
        }
        log.info("Assigning course with ID {} to faculty with ID {}", courseId, facultyId);
        FacultyCourseAssignment facultyCourseAssignment = DtoMapper.mapToFacultyCourseAssignment(facultyId, courseId);
        facultyCourseEnrollmentRepository.save(facultyCourseAssignment);
        log.info("Successfully assigned course with ID {} to faculty with ID {}", courseId, facultyId);
    }

    public void fallbackAssignCourse(Long facultyId, Long courseId, Throwable t) throws CourseException {
        log.error("Resilience fallback active for assignment. Faculty: {}, Course: {}. Reason: {}",
                facultyId, courseId, t.getMessage());

        if (t instanceof FacultyCourseEnrollmentException) {
            throw (FacultyCourseEnrollmentException) t;
        }
        if (t instanceof CourseException) {
            throw (CourseException) t;
        }
        throw new CourseException(
                "The enrollment service is currently unavailable or experiencing heavy load. Please try again later.",
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }
}
