package com.cts.engagement_service.application.dtoMapper;

import com.cts.dto.request.AttendanceRegistrationDto;
import com.cts.engagement_service.application.entity.Attendance;
import com.cts.util.UIDGeneratorUtils;

import java.time.LocalDateTime;

public class DtoMapper {
    public static Attendance attendanceDtoSeparator(AttendanceRegistrationDto attendanceRegistrationDto){
        Attendance attendance = new Attendance();
        Long attendanceId = UIDGeneratorUtils.uidGenerator();
        attendance.setAttendanceId(attendanceId);
        attendance.setStudentId(attendanceRegistrationDto.getStudentId());
        attendance.setCourseId(attendanceRegistrationDto.getCourseId());
        attendance.setLocalDateTime(LocalDateTime.now());
        return attendance;
    }
}
