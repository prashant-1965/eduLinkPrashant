package com.cts.course_service.application.service;

import com.cts.course_service.application.feign.FacultyFeign;
import com.cts.course_service.application.util.DtoMapper;
import com.cts.course_service.application.entity.Course;
import com.cts.course_service.application.feign.FacultyCourseEnrollmentFeign;
import com.cts.course_service.application.repository.CourseRepository;
import com.cts.dto.request.CourseRegistrationDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class CourseServiceImpl implements ICourseService {

    private final CourseRepository courseRepository;
    private final FacultyCourseEnrollmentFeign facultyCourseEnrollmentFeign;
    private final FacultyFeign facultyFeign;


    @Override
    @Transactional
    @CircuitBreaker(name = "courseRegister", fallbackMethod = "fallbackRegisterCourse")
    @Retry(name = "courseRegister")
    public String registerCourse(CourseRegistrationDto courseRegistrationDto) {
        log.info("Course registration has intercepted inside service");
        facultyFeign.getFacultyByFacultyId(courseRegistrationDto.getFacultyId());
        Course course = DtoMapper.courseDtoSeparator(courseRegistrationDto);
        log.error("Unable to separate faculty from courseRegistrationDto");
        course.setCourseStatus("ACTIVE");
        facultyCourseEnrollmentFeign.assignCourseToFaculty(courseRegistrationDto.getFacultyId(), course.getCourseId());
        courseRepository.save(course);
        log.info("Course with id {} saved successFully into database", course.getCourseId());
        return "Course has registered successFully with course Id: " + course.getCourseId();
    }

    public String fallbackRegisterCourse(CourseRegistrationDto dto, Throwable t) {
        log.error("Fallback triggered for course '{}'. Reason: {}", dto.getCourseTitle(), t.getMessage());
        return "Registration is temporarily unavailable. Please try again later. Error: " + t.getMessage();
    }
}
