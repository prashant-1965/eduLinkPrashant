package com.cts.academic_service.application.repository;

import com.cts.academic_service.application.entity.Exam;
import com.cts.dto.response.ExamProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query("select new com.cts.dto.response.ExamProjection(" +
            "e.examName, e.examLocalDateTime, e.examStatus, e.candidates) " +
            " from Exam e order by e.examLocalDateTime asc")
    List<ExamProjection> findAllExams();

    @Modifying
    @Transactional
    @Query("delete from Exam e where e.examId = :examId")
    void deleteByExamId(Long examId);
}
