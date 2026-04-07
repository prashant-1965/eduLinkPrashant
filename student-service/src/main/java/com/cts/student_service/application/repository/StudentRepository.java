package com.cts.student_service.application.repository;

import com.cts.student_service.application.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {

    @Query("select count(s) > 0 from Student s where s.studentId = :studentId")
    boolean existsByStudentId(Long studentId);
}
