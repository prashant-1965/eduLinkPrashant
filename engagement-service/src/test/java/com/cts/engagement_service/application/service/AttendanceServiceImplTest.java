package com.cts.engagement_service.application.service;

import com.cts.dto.request.AttendanceRegistrationDto;
import com.cts.dto.response.CourseAttendanceProjection;
import com.cts.engagement_service.application.entity.Attendance;
import com.cts.engagement_service.application.feign.CourseFeign;
import com.cts.engagement_service.application.feign.StudentCourseEnrollmentFeign;
import com.cts.engagement_service.application.feign.StudentFeign;
import com.cts.engagement_service.application.repository.AttendanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceImplTest {

    @Mock
    private AttendanceRepository attendanceRepository;
    @Mock
    private StudentFeign studentFeign;
    @Mock
    private CourseFeign courseFeign;
    @Mock
    private StudentCourseEnrollmentFeign studentCourseEnrollmentFeign;

    private AttendanceServiceImpl attendanceService;

    @BeforeEach
    void setUp() {
        attendanceService = new AttendanceServiceImpl(attendanceRepository, studentFeign, courseFeign, studentCourseEnrollmentFeign);
    }

    @Test
    @DisplayName("Register Attendance - Should verify all Feign clients and save attendance")
    void registerAttendanceByStudentId_Success() {
        AttendanceRegistrationDto dto = new AttendanceRegistrationDto();
        dto.setStudentId(1L);
        dto.setCourseId(101L);
        String result = attendanceService.registerAttendanceByStudentId(dto);
        verify(studentFeign).checkStudentExistByStudentId(1L);
        verify(courseFeign).checkCourseExistByCourseId(101L);
        verify(studentCourseEnrollmentFeign).checkStudentExistInCourse(1L, 101L);
        verify(attendanceRepository, times(1)).save(any(Attendance.class));

        assertEquals("Attendance recorded successFully!", result);
    }

    @Test
    @DisplayName("Find Attendance By Course - Should calculate percentage correctly")
    void findAttendanceByCourse_Success() {
        // Arrange
        Long studentId = 1L;
        Long courseId = 101L;
        LocalDateTime firstDate = LocalDateTime.now().minusDays(10);

        when(studentCourseEnrollmentFeign.getCoursesListByStudentId(studentId)).thenReturn(List.of(courseId));
        when(courseFeign.findCourseTitleByCourseId(courseId)).thenReturn("Java Programming");
        when(attendanceRepository.countAttendanceByIdAndStudentId(courseId, studentId)).thenReturn(5L);
        when(attendanceRepository.findFirstEnrollmentDate(courseId, studentId)).thenReturn(firstDate);
        List<CourseAttendanceProjection> result = attendanceService.findAttendanceByCourse(studentId);
        assertFalse(result.isEmpty());
        CourseAttendanceProjection projection = result.getFirst();
        assertEquals(courseId, projection.getCourseId());
        assertEquals("Java Programming", projection.getCourseTitle());
        assertTrue(projection.getAttendancePercentage() > 0);
        verify(studentFeign).checkStudentExistByStudentId(studentId);
    }

    @Test
    @DisplayName("Find Attendance By Course - Should handle zero attendance records")
    void findAttendanceByCourse_ZeroAttendance() {
        Long studentId = 1L;
        Long courseId = 101L;
        when(studentCourseEnrollmentFeign.getCoursesListByStudentId(studentId)).thenReturn(List.of(courseId));
        when(attendanceRepository.countAttendanceByIdAndStudentId(courseId, studentId)).thenReturn(0L);
        List<CourseAttendanceProjection> result = attendanceService.findAttendanceByCourse(studentId);
        assertEquals(0.0, result.getFirst().getAttendancePercentage());
        verify(attendanceRepository, never()).findFirstEnrollmentDate(anyLong(), anyLong());
    }
}