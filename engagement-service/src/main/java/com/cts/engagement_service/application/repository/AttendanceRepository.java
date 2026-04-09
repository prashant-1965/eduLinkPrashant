package com.cts.engagement_service.application.repository;

import com.cts.engagement_service.application.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    @Query("select count(a) from Attendance a where courseId = :courseId and a.studentId = :studentId")
    Long countAttendanceByIdAndStudentId(Long courseId, Long studentId);


    @Query("SELECT min(a.localDateTime) from Attendance a where a.courseId = :courseId and a.studentId = :studentId")
    LocalDateTime findFirstEnrollmentDate(Long courseId, Long studentId);
}
