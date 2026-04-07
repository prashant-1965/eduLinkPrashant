package com.cts.enrollment_service.application.service;

public interface IFacultyCourseEnrollmentService {
        void assignCourseToFaculty(Long facultyId, Long courseId);
}
