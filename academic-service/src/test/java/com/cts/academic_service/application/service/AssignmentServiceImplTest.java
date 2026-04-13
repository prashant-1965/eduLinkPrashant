package com.cts.academic_service.application.service;

import com.cts.academic_service.application.entity.AssignmentStatus;
import com.cts.academic_service.application.repository.AssignmentStatusRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceImplTest {

    @Mock
    private AssignmentStatusRepository repository;

    @InjectMocks
    private AssignmentServiceImpl assignmentService;

    @Test
    @DisplayName("Should return list of assignments for a specific student and course")
    void getAssignmentsForStudent_Success() {
        // Arrange
        Long courseId = 101L;
        Long studentId = 501L;
        List<AssignmentStatus> mockList = List.of(new AssignmentStatus(), new AssignmentStatus());
        when(repository.findByStudentIdAndCourseId(studentId, courseId)).thenReturn(mockList);

        // Act
        List<AssignmentStatus> result = assignmentService.getAssignmentsForStudent(courseId, studentId);

        // Assert
        assertEquals(2, result.size());
        verify(repository, times(1)).findByStudentIdAndCourseId(studentId, courseId);
    }

    @Test
    @DisplayName("Should update status to Completed when assignment exists")
    void completeAssignment_Success() {
        Long assignmentId = 1L;
        AssignmentStatus assignment = new AssignmentStatus();
        assignment.setStatus("Pending");
        when(repository.findById(assignmentId)).thenReturn(Optional.of(assignment));
        assignmentService.completeAssignment(assignmentId, 999L);
        assertEquals("Completed", assignment.getStatus());
        verify(repository).save(assignment);
    }

    @Test
    @DisplayName("Should throw RuntimeException when assignment is not found")
    void completeAssignment_NotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () ->
                assignmentService.completeAssignment(1L, 1L)
        );
    }

    @Test
    @DisplayName("Should return true when all assignments are completed")
    void canStudentTakeExam_True() {
        AssignmentStatus a1 = new AssignmentStatus();
        a1.setStatus("Completed");
        AssignmentStatus a2 = new AssignmentStatus();
        a2.setStatus("Completed");
        when(repository.findByStudentIdAndCourseId(1L, 1L)).thenReturn(List.of(a1, a2));
        boolean canTakeExam = assignmentService.canStudentTakeExam(1L, 1L);
        assertTrue(canTakeExam);
    }

    @Test
    @DisplayName("Should return false if at least one assignment is not completed")
    void canStudentTakeExam_False() {
        AssignmentStatus a1 = new AssignmentStatus();
        a1.setStatus("Completed");
        AssignmentStatus a2 = new AssignmentStatus();
        a2.setStatus("Pending");
        when(repository.findByStudentIdAndCourseId(1L, 1L)).thenReturn(List.of(a1, a2));
        boolean canTakeExam = assignmentService.canStudentTakeExam(1L, 1L);
        assertFalse(canTakeExam);
    }
}
