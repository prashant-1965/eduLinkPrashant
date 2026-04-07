package com.cts.enrollment_service.application.service;

import java.util.List;

public interface IFacultyCourseEnrollmentService {
        void assignCourseToFaculty(Long facultyId, Long courseId);
        List<Long> getCoursesListByFacultyId(Long facultyId);
        int getFacultyCourseCount(Long facultyId);
        Long findFacultyIdByCourseId(Long courseId);
}
