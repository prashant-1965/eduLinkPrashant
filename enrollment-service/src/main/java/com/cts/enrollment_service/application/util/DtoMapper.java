package com.cts.enrollment_service.application.util;

import com.cts.enrollment_service.application.entity.FacultyCourseAssignment;
import com.cts.enrollment_service.application.entity.StudentCourseEnrollment;

public class DtoMapper {
    public static FacultyCourseAssignment mapToFacultyCourseAssignment(Long facultyId, Long courseId) {
        FacultyCourseAssignment facultyCourseAssignment = new FacultyCourseAssignment();
        facultyCourseAssignment.setCourseId(courseId);
        facultyCourseAssignment.setFacultyId(facultyId);
        return facultyCourseAssignment;
    }

    public static StudentCourseEnrollment mapToStudentCourseAssignment(Long courseId, Long studentId) {
        StudentCourseEnrollment studentCourseEnrollment = new StudentCourseEnrollment();
        studentCourseEnrollment.setCourseId(courseId);
        studentCourseEnrollment.setStudentId(studentId);
        return studentCourseEnrollment;
    }
}
