package com.cts.academic_service.application.service;


import com.cts.academic_service.application.entity.Grade;
import com.cts.academic_service.application.feign.StudentFeign;
import com.cts.academic_service.application.repository.GradeRepository;
import com.cts.classexception.GradeException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class GradeServiceImpl implements IGradeService{
    private final GradeRepository gradeRepository;
    private final StudentFeign studentFeign;

    @Override
    public String findGradeStatus(Long gradeId) throws GradeException {
        log.info("Fetching grade status for ID: {}", gradeId);
        Optional<Grade> grade = gradeRepository.findGradeById(gradeId);
        if(grade.isEmpty()){
            log.error("Grade lookup failed: No assignment found with ID {}", gradeId);
            throw new GradeException("NO assignment available with id: "+gradeId, HttpStatus.NOT_FOUND);
        }
        String status = gradeRepository.findGradeStatus(gradeId);
        log.debug("Successfully retrieved status '{}' for grade ID: {}", status, gradeId);
        return status;
    }

    @Override
    public double findTotalGradeByStudentId(Long studentId) throws GradeException {
        studentFeign.checkStudentExistByStudentId(studentId);
        log.info("Calculating total grade for student ID: {}", studentId);
        Optional<Grade> grade = gradeRepository.checkStudentAvailableInGrade(studentId);
        if(grade.isEmpty()){
            log.warn("Grade calculation aborted: Student ID {} has no recorded tests.", studentId);
            throw new GradeException(studentId+" is not given any test yet!",HttpStatus.NOT_FOUND);
        }
        double totalGrade = gradeRepository.findGradeByStudentId(studentId);
        log.info("Total grade for student ID {}: {}", studentId, totalGrade);
        return totalGrade;
    }
}
