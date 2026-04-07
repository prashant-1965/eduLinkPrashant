package com.cts.enrollment_service.application.repository;

import com.cts.enrollment_service.application.entity.StudentCourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCourseEnrollmentRepository extends JpaRepository<StudentCourseEnrollment,Long> {

    @Query("select count(sce) > 0 from StudentCourseEnrollment sce where sce.studentId = :studentId and sce.courseId = :courseId")
    boolean isAlreadyEnrolled(Long studentId, Long courseId);

    @Query("select sce.courseId from StudentCourseEnrollment sce where sce.studentId = :studentId")
    List<Long> findCourseIdsByStudentId(Long studentId);
}
