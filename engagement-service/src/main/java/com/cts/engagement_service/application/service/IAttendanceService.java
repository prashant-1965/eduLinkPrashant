package com.cts.engagement_service.application.service;

import com.cts.dto.request.AttendanceRegistrationDto;
import com.cts.dto.response.CourseAttendanceProjection;

import java.util.List;

public interface IAttendanceService {
    List<CourseAttendanceProjection> findAttendanceByCourse(Long studentId);
    String registerAttendanceByStudentId(AttendanceRegistrationDto attendanceRegistrationDto);
}
