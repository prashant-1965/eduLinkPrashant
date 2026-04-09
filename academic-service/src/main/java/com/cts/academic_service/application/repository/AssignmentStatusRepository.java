package com.cts.academic_service.application.repository;

import com.cts.academic_service.application.entity.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentStatusRepository extends JpaRepository<AssignmentStatus, Long> {
    List<AssignmentStatus> findByStudentIdAndCourseId(Long studentId, Long courseId);
}
