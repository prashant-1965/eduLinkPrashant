package com.cts.engagement_service.application.service;

import com.cts.classexception.FeedbackException;
import com.cts.dto.request.FeedbackDto;
import com.cts.dto.response.FeedbackProjection;
import com.cts.engagement_service.application.entity.FeedBack;
import com.cts.engagement_service.application.feign.FacultyFeign;
import com.cts.engagement_service.application.feign.StudentFeign;
import com.cts.engagement_service.application.repository.FeedBackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedBackServiceImplTest {

    @Mock
    private FeedBackRepository feedBackRepository;
    @Mock
    private StudentFeign studentFeign;
    @Mock
    private FacultyFeign facultyFeign;

    private FeedBackServiceImpl feedBackService;

    @BeforeEach
    void setUp() {
        feedBackService = new FeedBackServiceImpl(feedBackRepository, studentFeign, facultyFeign);
    }

    @Test
    @DisplayName("Register Feedback - Student Type - Should call StudentFeign and Save")
    void registerFeedback_StudentSuccess() {
        FeedbackDto dto = new FeedbackDto();
        dto.setAppUserRoleId(10L);
        dto.setReviewerType("STUDENT");
        when(studentFeign.getStudentNameByStudentId(10L)).thenReturn("John Doe");
        String result = feedBackService.registerFeedback(dto);
        verify(studentFeign).checkStudentExistByStudentId(10L);
        verify(studentFeign).getStudentNameByStudentId(10L);
        verifyNoInteractions(facultyFeign); // FacultyFeign should NOT be touched
        verify(feedBackRepository).save(any(FeedBack.class));
        assertEquals("Thank you for your feedback!", result);
    }

    @Test
    @DisplayName("Register Feedback - Faculty Type - Should call FacultyFeign and Save")
    void registerFeedback_FacultySuccess() {
        FeedbackDto dto = new FeedbackDto();
        dto.setAppUserRoleId(20L);
        dto.setReviewerType("FACULTY");
        when(facultyFeign.getFacultyNameByFacultyId(20L)).thenReturn("Prof. Smith");
        String result = feedBackService.registerFeedback(dto);
        verify(facultyFeign).checkFacultyExistByFacultyId(20L);
        verify(facultyFeign).getFacultyNameByFacultyId(20L);
        verifyNoInteractions(studentFeign); // StudentFeign should NOT be touched
        verify(feedBackRepository).save(any(FeedBack.class));
        assertEquals("Thank you for your feedback!", result);
    }

    @Test
    @DisplayName("Register Feedback - Invalid Type - Should throw BadRequest Exception")
    void registerFeedback_InvalidType() {
        FeedbackDto dto = new FeedbackDto();
        dto.setReviewerType("ADMIN");
        FeedbackException ex = assertThrows(FeedbackException.class, () ->
                feedBackService.registerFeedback(dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        assertEquals("Invalid feedback type", ex.getMessage());
    }

    @Test
    @DisplayName("Find Feedback List - Should return list when records exist")
    void findFeedBackList_Success() {
        FeedbackProjection mockProj = mock(FeedbackProjection.class);
        when(feedBackRepository.findFeedBackList()).thenReturn(List.of(mockProj));
        List<FeedbackProjection> result = feedBackService.findFeedBackList();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Find Feedback List - Should throw NotFound Exception when empty")
    void findFeedBackList_EmptyThrowsException() {
        when(feedBackRepository.findFeedBackList()).thenReturn(Collections.emptyList());
        FeedbackException ex = assertThrows(FeedbackException.class, () ->
                feedBackService.findFeedBackList());
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
    }
}