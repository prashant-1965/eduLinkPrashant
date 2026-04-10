package com.cts.academic_service.application.repository;

import com.cts.academic_service.application.entity.Exam;
import com.cts.dto.response.ExamProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query("SELECT new com.cts.dto.response.ExamProjection(" +
            "e.examName, e.examLocalDateTime, e.examStatus, e.candidates) " +
            "FROM Exam e ORDER BY e.examLocalDateTime ASC")
    List<ExamProjection> findAllExams();

    @Query("delete from Exam e where e.examId = :examId")
    void deleteByExamId(Long examId);
}
