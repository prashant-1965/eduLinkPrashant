package com.cts.course_service.application.service;

import com.cts.classexception.CourseException;
import com.cts.classexception.LearningMaterialException;
import com.cts.course_service.application.entity.Course;
import com.cts.course_service.application.projection.LearningCourseMaterialProjection;
import com.cts.course_service.application.repository.CourseRepository;
import com.cts.course_service.application.repository.LearningMaterialRepository;
import com.cts.dto.request.LearningMaterialRegistrationDto;
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
class LearningMaterialServiceImplTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private LearningMaterialRepository learningMaterialRepository;

    private LearningMaterialServiceImpl learningMaterialService;

    @BeforeEach
    void setUp() {
        learningMaterialService = new LearningMaterialServiceImpl(courseRepository, learningMaterialRepository);
    }

    @Test
    @DisplayName("Register Material - Should throw CourseException when course not found")
    void registerLearningMaterial_CourseNotFound() {
        Long courseId = 1L;
        LearningMaterialRegistrationDto dto = new LearningMaterialRegistrationDto();
        dto.setCourseId(courseId);
        when(courseRepository.findCourseById(courseId)).thenReturn(Optional.empty());
        CourseException ex = assertThrows(CourseException.class, () ->
                learningMaterialService.registerLearningMaterial(dto)
        );
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
    }

    @Test
    @DisplayName("Register Material - Should throw exception if material already exists")
    void registerLearningMaterial_AlreadyExists() {
        Long courseId = 1L;
        LearningMaterialRegistrationDto dto = new LearningMaterialRegistrationDto();
        dto.setCourseId(courseId);
        when(courseRepository.findCourseById(courseId)).thenReturn(Optional.of(new Course()));
        when(learningMaterialRepository.checkExistingLearningMaterial(courseId)).thenReturn(true);
        LearningMaterialException ex = assertThrows(LearningMaterialException.class, () ->
                learningMaterialService.registerLearningMaterial(dto)
        );
        assertEquals(HttpStatus.CONFLICT, ex.getHttpStatus());
    }

    @Test
    @DisplayName("Find Materials - Should return projection when found")
    void findMaterialsByCourseId_Success() {
        Long courseId = 1L;
        LearningCourseMaterialProjection mockProjection = mock(LearningCourseMaterialProjection.class);
        when(courseRepository.findCourseById(courseId)).thenReturn(Optional.of(new Course()));
        when(learningMaterialRepository.findMaterialsByCourseId(courseId)).thenReturn(Optional.of(mockProjection));
        LearningCourseMaterialProjection result = learningMaterialService.findMaterialsByCourseId(courseId);
        assertNotNull(result);
        verify(learningMaterialRepository).findMaterialsByCourseId(courseId);
    }

    @Test
    @DisplayName("Find Materials - Should throw LearningMaterialException when projection is empty")
    void findMaterialsByCourseId_NotFound() {
        Long courseId = 1L;
        when(courseRepository.findCourseById(courseId)).thenReturn(Optional.of(new Course()));
        when(learningMaterialRepository.findMaterialsByCourseId(courseId)).thenReturn(Optional.empty());
        LearningMaterialException ex = assertThrows(LearningMaterialException.class, () ->
                learningMaterialService.findMaterialsByCourseId(courseId)
        );
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
    }

    @Test
    @DisplayName("Get File - Should throw RuntimeException when ID is invalid")
    void getFileFromProjection_MaterialNotFound() {
        Long materialId = 100L;
        when(learningMaterialRepository.findById(materialId)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                learningMaterialService.getFileFromProjection(materialId)
        );
        assertEquals("Material not found", ex.getMessage());
    }
}