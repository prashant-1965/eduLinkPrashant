package com.cts.enrollment_service.application.util;

import com.cts.enrollment_service.application.entity.FacultyCourseAssignment;

public class DtoMapper {
    public static FacultyCourseAssignment mapToFacultyCourseAssignment(Long courseId, Long facultyId) {
        FacultyCourseAssignment facultyCourseAssignment = new FacultyCourseAssignment();
        facultyCourseAssignment.setCourseId(courseId);
        facultyCourseAssignment.setFacultyId(facultyId);
        return facultyCourseAssignment;
    }
}
