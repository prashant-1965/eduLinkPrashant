package com.cts.academic_service.application.service;

import com.cts.academic_service.application.entity.AssignmentStatus;
import com.cts.academic_service.application.repository.AssignmentStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AssignmentServiceImpl implements IAssignmentService {
    private final AssignmentStatusRepository assignmentStatusRepository;

    @Override
    public List<AssignmentStatus> getAssignmentsForStudent(Long courseId, Long studentId) {
        return assignmentStatusRepository.findByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public void completeAssignment(Long assignmentId, Long studentId) {
        AssignmentStatus assignment = assignmentStatusRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        assignment.setStatus("Completed");
        assignmentStatusRepository.save(assignment);
    }

    @Override
    public boolean canStudentTakeExam(Long courseId, Long studentId) {
        List<AssignmentStatus> assignments = getAssignmentsForStudent(courseId, studentId);
        return assignments.stream().allMatch(a -> "Completed".equals(a.getStatus()));
    }
}
