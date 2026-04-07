package com.cts.enrollment_service.application.repository;

import com.cts.enrollment_service.application.entity.FacultyCourseAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyCourseEnrollmentRepository extends JpaRepository<FacultyCourseAssignment,Long> {

    @Query("SELECT COUNT(f) > 0 FROM FacultyCourseAssignment f WHERE f.facultyId = :facultyId AND f.courseId = :courseId")
    boolean existsByFacultyIdAndCourseId(@Param("facultyId") Long facultyId, @Param("courseId") Long courseId);
}
