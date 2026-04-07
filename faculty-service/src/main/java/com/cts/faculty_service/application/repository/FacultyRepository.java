package com.cts.faculty_service.application.repository;

import com.cts.faculty_service.application.entity.Faculty;
import com.cts.faculty_service.application.projection.FacultyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty,Long> {
    @Query("select f from Faculty f where f.facultyId = :facultyId")
    Optional<Faculty> findFacultyById(@Param("facultyId") Long facultyId);

    @Query("select new com.cts.faculty_service.application.projection.FacultyDetail(f.facultyRating,f.facultyYearOfExperience)"+
            " from Faculty f where f.facultyId = :facultyId")
    Optional<FacultyDetail> findFacultyDetailProjectionByFacultyId(@Param("facultyId") Long facultyId);
}
