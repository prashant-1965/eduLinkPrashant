package com.cts.enrollment_service.application.service;


import com.cts.classexception.FacultyCourseEnrollmentException;
import com.cts.enrollment_service.application.repository.FacultyCourseEnrollmentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class FacultyCourseEnrollmentServiceImpl implements IFacultyCourseEnrollmentService{

    private final FacultyCourseEnrollmentRepository facultyCourseEnrollmentRepository;

    @Override
    public void assignCourseToFaculty(Long facultyId, Long courseId)throws FacultyCourseEnrollmentException {
        boolean isAlreadyAssigned = facultyCourseEnrollmentRepository.existsByFacultyIdAndCourseId(facultyId, courseId);
        if (isAlreadyAssigned) {
            log.warn("Course with ID {} is already assigned to faculty with ID {}", courseId, facultyId);
            throw new FacultyCourseEnrollmentException("Course with ID " + courseId + " is already assigned to faculty with ID " + facultyId, HttpStatus.CONFLICT);
        }
        log.info("Assigning course with ID {} to faculty with ID {}", courseId, facultyId);
        facultyCourseEnrollmentRepository.assignCourseToFaculty(facultyId, courseId);
        log.info("Successfully assigned course with ID {} to faculty with ID {}", courseId, facultyId);
    }
}
