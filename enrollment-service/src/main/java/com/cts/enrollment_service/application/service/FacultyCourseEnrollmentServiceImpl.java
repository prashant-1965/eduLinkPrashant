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

import java.util.List;
import java.util.Optional;

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

    @CircuitBreaker(name = "enrollmentService", fallbackMethod = "fallbackAssignCourse")
    @Retry(name = "enrollmentService")
    @Override
    public List<Long> getCoursesListByFacultyId(Long facultyId) throws FacultyCourseEnrollmentException {
        List<Long> courseIdList = facultyCourseEnrollmentRepository.findCourseIdByFacultyId(facultyId);
        if (courseIdList.isEmpty()) {
            log.info("No courses found for faculty with ID {}", facultyId);
            throw new FacultyCourseEnrollmentException("No courses found for faculty with ID " + facultyId, HttpStatus.NOT_FOUND);
        }
        log.info("Found {} courses for faculty with ID {}", courseIdList.size(), facultyId);
        return courseIdList;
    }

    @Override
    public int getFacultyCourseCount(Long facultyId) {
        return facultyCourseEnrollmentRepository.countByFacultyId(facultyId);
    }

    @Override
    public Long findFacultyIdByCourseId(Long courseId) throws FacultyCourseEnrollmentException {
        Optional<FacultyCourseAssignment> assignmentOpt = facultyCourseEnrollmentRepository.findFacultyIdByCourseId(courseId);
        if (assignmentOpt.isEmpty()) {
            log.info("No faculty found for course with ID {}", courseId);
            throw new FacultyCourseEnrollmentException("No faculty found for course with ID " + courseId, HttpStatus.NOT_FOUND);
        }
        Long facultyId = assignmentOpt.get().getFacultyId();
        log.info("Found faculty with ID {} for course with ID {}", facultyId, courseId);
        return facultyId;
   }

    public List<Long> fallbackGetCoursesListByFacultyId(Long facultyId, Throwable t) throws FacultyCourseEnrollmentException {
        log.error("Resilience fallback active for getCoursesListByFacultyId. Faculty: {}. Reason: {}",
                facultyId, t.getMessage());

        if (t instanceof FacultyCourseEnrollmentException) {
            throw (FacultyCourseEnrollmentException) t;
        }
        throw new FacultyCourseEnrollmentException(
                "The enrollment service is currently unavailable or experiencing heavy load. Please try again later.",
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    public List<Long> fallbackAssignCourse(Long facultyId, Throwable t) throws CourseException {
        log.error("Resilience fallback active for getCoursesList. Faculty: {}. Reason: {}",
                facultyId, t.getMessage());
        if (t instanceof FacultyCourseEnrollmentException) {
            throw (FacultyCourseEnrollmentException) t;
        }
        throw new CourseException(
                "The enrollment service is currently unavailable. Please try again later.",
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }
}
