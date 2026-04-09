package com.cts.academic_service.application.service;

public interface IGradeService {
    String findGradeStatus(Long gradeId);
    double findTotalGradeByStudentId(Long studentId);
}
