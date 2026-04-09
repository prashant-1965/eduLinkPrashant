package com.cts.course_service.application.repository;

import com.cts.course_service.application.entity.Course;
import com.cts.course_service.application.projection.CourseDetailProjection;
import com.cts.dto.response.CourseProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {
    @Query(" select new com.cts.dto.response.CourseProjection(c.courseId, c.courseTitle," +
            " c.courseSubject,c.courseGradeLevel,c.courseCredit,c.courseStatus,c.courseRating) from Course c where c.courseStatus='ACTIVE'")
    List<CourseProjection> findAllAvailableCourse();

    @Query("SELECT new com.cts.dto.response.CourseProjection(c.courseId, c.courseTitle," +
            " c.courseSubject,c.courseGradeLevel,c.courseCredit,c.courseStatus,c.courseRating) FROM Course c where c.courseId = :courseId")
    Optional<CourseProjection> findByCourseId(@Param("courseId") Long courseId);

//    @Query("select new com.cts.eduLink.application.projection.CourseSummaryProjection(c.id, c.courseId, c.courseTitle)"+" from Course c"+
//            " inner join c.studentSet s where s.studentId = :studentId")
//    List<CourseSummaryProjection> findCourseSummaryListByStudentId(@Param("studentId") Long studentId);

    @Query("select c from Course c where c.courseId = :courseId")
    Optional<Course> findCourseById(@Param("courseId") Long courseId);

    @Query(" select new com.cts.course_service.application.projection.CourseDetailProjection(c.courseTitle," +
            " c.courseGradeLevel,c.courseRating) from Course c where c.courseId = :courseId")
    Optional<CourseDetailProjection> findCourseListByCourseId(@Param("courseId") Long courseId);

}
