package com.cts.enrollment_service.application.repository;

import com.cts.enrollment_service.application.entity.FacultyCourseAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyCourseEnrollmentRepository extends JpaRepository<FacultyCourseAssignment,Long> {
    void assignCourseToFaculty(Long facultyId, Long courseId);

    boolean existsByFacultyIdAndCourseId(Long facultyId, Long courseId);
}
