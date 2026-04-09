package com.cts.faculty_service.application.repository;

import com.cts.dto.response.FacultyDetailProjection;
import com.cts.faculty_service.application.entity.Faculty;
import com.cts.faculty_service.application.projection.FacultyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty,Long> {
    @Query("select f from Faculty f where f.facultyId = :facultyId")
    Optional<Faculty> findFacultyById(@Param("facultyId") Long facultyId);

    @Query("select new com.cts.faculty_service.application.projection.FacultyDetail(f.facultyRating,f.facultyYearOfExperience)"+
            " from Faculty f where f.facultyId = :facultyId")
    Optional<FacultyDetail> findFacultyDetailProjectionByFacultyId(@Param("facultyId") Long facultyId);


//    @Query("SELECT e FROM Exam e " +
//            "JOIN e.course c " +
//            "JOIN c.facultySet f " +
//            "WHERE f.facultyId = :facultyId " +
//            "AND e.examLocalDateTime < CURRENT_TIMESTAMP " +
//            "ORDER BY e.examLocalDateTime ASC")
//    List<Exam> findUpcomingExamsByFacultyId(
//            @Param("facultyId") Long facultyId
//    );
//
//    @Query("SELECT COUNT(e) FROM Exam e " +
//            "JOIN e.course c " +
//            "JOIN c.facultySet f " +
//            "WHERE f.facultyId = :facultyId " +
//            "AND e.examLocalDateTime < CURRENT_TIMESTAMP")
//    int getUpcomingExamsCount(@Param("facultyId") Long facultyId);
//
}
