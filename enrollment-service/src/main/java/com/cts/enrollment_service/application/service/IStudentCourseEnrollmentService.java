package com.cts.enrollment_service.application.service;

import java.util.List;

public interface IStudentCourseEnrollmentService {
    void assignCourseToStudent(Long studentId, Long courseId);
    List<Long> getEnrolledCourseIdsByStudentId(Long studentId);
}
