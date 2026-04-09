package com.cts.academic_service.application.service;


import com.cts.academic_service.application.entity.Exam;
import com.cts.academic_service.application.feign.CourseFeign;
import com.cts.academic_service.application.repository.ExamRepository;
import com.cts.academic_service.application.util.DtoMapper;
import com.cts.classexception.ExamException;
import com.cts.dto.request.ExamCreationRequestDto;
import com.cts.dto.response.ExamProjection;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ExamServiceImpl implements IExamService {

    private final ExamRepository examRepository;
    private final CourseFeign courseFeign;


    @Override
    public String createExam(ExamCreationRequestDto examCreationRequestDto) {
        log.info("Creating a new exam: {}", examCreationRequestDto.getExamName());
        courseFeign.checkCourseExistByCourseId(examCreationRequestDto.getCourseId());
        Exam exam = DtoMapper.ExamDtoSeparator(examCreationRequestDto);
        examRepository.save(exam);
        log.info("Exam created successfully: {}", examCreationRequestDto.getExamName());
        return "Exam created successfully with ID: " + exam.getExamId();
    }

    @Override
    @Transactional
    public String deleteExam(Long examId) {
        log.info("Deletion request initiated for Exam ID: {}", examId);
        examRepository.deleteByExamId(examId);
        log.info("Exam ID: {} deleted successfully", examId);
        return "Exam deleted successfully!";
    }
    @Override
    public List<ExamProjection> findAllExams() throws ExamException {
        log.info("User has requested to display all exams via projection");
        List<ExamProjection> examProjections = examRepository.findAllExams();
        if (examProjections.isEmpty()) {
            log.error("No exams are currently available in the system");
            throw new ExamException("No exams found!", HttpStatus.NOT_FOUND);
        }
        log.info("Exam list accessed successfully. Total exams found: {}. First exam: {}", examProjections.size(), examProjections.getFirst());
        return examProjections;
    }
}
