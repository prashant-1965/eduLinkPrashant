package com.cts.academic_service.application.service;

import com.cts.dto.request.ExamCreationRequestDto;
import com.cts.dto.response.ExamProjection;

import java.util.List;

public interface IExamService {
    String createExam(ExamCreationRequestDto examCreationRequestDto);
    List<ExamProjection> findAllExams();
    String deleteExam(Long examId);

}
