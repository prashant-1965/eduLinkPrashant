package com.cts.engagement_service.application.dtoMapper;

import com.cts.dto.request.AttendanceRegistrationDto;
import com.cts.engagement_service.application.entity.Attendance;

import java.time.LocalDateTime;

public class DtoMapper {
    public static Attendance attendanceDtoSeparator(AttendanceRegistrationDto attendanceRegistrationDto){
        Attendance attendance = new Attendance();
        attendance.setLocalDateTime(LocalDateTime.now());
        return attendance;
    }
}
