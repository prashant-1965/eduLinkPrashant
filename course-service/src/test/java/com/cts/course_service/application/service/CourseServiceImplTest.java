package com.cts.course_service.application.service;

import com.cts.classexception.CourseException;
import com.cts.course_service.application.entity.Course;
import com.cts.course_service.application.feign.CourseEnrollmentFeign;
import com.cts.course_service.application.feign.FacultyFeign;
import com.cts.course_service.application.feign.StudentFeign;
import com.cts.course_service.application.repository.CourseRepository;
import com.cts.dto.request.CourseRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private CourseEnrollmentFeign courseEnrollmentFeign;
    @Mock
    private FacultyFeign facultyFeign;
    @Mock
    private StudentFeign studentFeign;
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        courseService = new CourseServiceImpl(courseRepository, courseEnrollmentFeign, facultyFeign, studentFeign);
    }

    @Test
    @DisplayName("Check Course Exist - Should throw exception when course does not exist")
    void checkCourseExistByCourseId_ThrowsException() {
        // Arrange
        Long courseId = 1L;
        when(courseRepository.existsByCourseId(courseId)).thenReturn(false);

        CourseException exception = assertThrows(CourseException.class, () ->
                courseService.checkCourseExistByCourseId(courseId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertTrue(exception.getMessage().contains("not registered"));
    }

    @Test
    @DisplayName("Delete Course - Should set status to INACTIVE")
    void deleteCourse_Success() {
        Long courseId = 10L;
        Course mockCourse = new Course();
        mockCourse.setCourseId(courseId);
        mockCourse.setCourseStatus("ACTIVE");
        when(courseRepository.findCourseById(courseId)).thenReturn(Optional.of(mockCourse));
        String result = courseService.deleteCourse(courseId);
        assertEquals("INACTIVE", mockCourse.getCourseStatus());
        assertTrue(result.contains("deleted successfully"));
    }

    @Test
    @DisplayName("Update Rating - Should calculate new rating and increment count")
    void updateCourseRating_Success() {
        Long courseId = 1L;
        Course course = new Course();
        course.setCourseRating(4.0);
        course.setTotalCourseRatingCount(1L);
        when(courseRepository.findCourseById(courseId)).thenReturn(Optional.of(course));
        courseService.updateCourseRating(courseId, 5.0);
        assertEquals(2, course.getTotalCourseRatingCount());
        assertEquals(4.5, course.getCourseRating());
    }

    @Test
    @DisplayName("Register Course - Should call Feign and save to DB")
    void registerCourse_Success() {
        CourseRegistrationDto dto = new CourseRegistrationDto();
        dto.setFacultyId(50L);
        dto.setCourseTitle("Java Testing");
        String result = courseService.registerCourse(dto);
        verify(facultyFeign).checkFacultyByFacultyId(50L);
        verify(courseRepository).save(any(Course.class));
        verify(courseEnrollmentFeign).assignCourseToFaculty(eq(50L), any());
        assertTrue(result.contains("registered successFully"));
    }

    @Test
    @DisplayName("Fallback Method - Should return error message")
    void fallbackRegisterCourse_ReturnsMessage() {
        CourseRegistrationDto dto = new CourseRegistrationDto();
        dto.setCourseTitle("Math");
        Throwable t = new RuntimeException("Connection Timeout");
        String result = courseService.fallbackRegisterCourse(dto, t);
        assertTrue(result.contains("temporarily unavailable"));
        assertTrue(result.contains("Connection Timeout"));
    }
}