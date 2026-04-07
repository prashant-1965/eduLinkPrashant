package com.cts.enrollment_service.application.repository;

import com.cts.enrollment_service.application.entity.FacultyCourseAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyCourseEnrollmentRepository extends JpaRepository<FacultyCourseAssignment,Long> {

    @Query("SELECT COUNT(f) > 0 from FacultyCourseAssignment f where f.facultyId = :facultyId and f.courseId = :courseId")
    boolean existsByFacultyIdAndCourseId(@Param("facultyId") Long facultyId, @Param("courseId") Long courseId);

    @Query("select f.courseId from FacultyCourseAssignment f where f.facultyId = :facultyId")
    List<Long> findCourseIdByFacultyId(@Param("facultyId") Long facultyId);

    @Query("SELECT COUNT(f) from FacultyCourseAssignment f where f.facultyId = :facultyId")
    int countByFacultyId(@Param("facultyId") Long facultyId);

    @Query("select f from FacultyCourseAssignment f where f.courseId = :courseId")
    Optional<FacultyCourseAssignment> findFacultyIdByCourseId(@Param("courseId") Long courseId);
}
