package com.cts.academic_service.application.service;

import com.cts.academic_service.application.entity.AssignmentStatus;

import java.util.List;

public interface IAssignmentService {
    List<AssignmentStatus> getAssignmentsForStudent(Long courseId, Long studentId);
    void completeAssignment(Long assignmentId, Long studentId);
    boolean canStudentTakeExam(Long courseId, Long studentId);

}
