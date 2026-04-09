package com.cts.engagement_service.application.service;

import com.cts.dto.request.AttendanceRegistrationDto;
import com.cts.dto.response.CourseAttendanceProjection;
import com.cts.engagement_service.application.dtoMapper.DtoMapper;
import com.cts.engagement_service.application.entity.Attendance;
import com.cts.engagement_service.application.feign.CourseFeign;
import com.cts.engagement_service.application.feign.StudentCourseEnrollmentFeign;
import com.cts.engagement_service.application.feign.StudentFeign;
import com.cts.engagement_service.application.repository.AttendanceRepository;
import com.cts.util.DateUtils;
import com.cts.util.AttendanceCalculator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements IAttendanceService{

    private final AttendanceRepository attendanceRepository;
    private final StudentFeign studentFeign;
    private final CourseFeign courseFeign;
    private final StudentCourseEnrollmentFeign studentCourseEnrollmentFeign;


    @Override
    @Transactional
    public String registerAttendanceByStudentId(AttendanceRegistrationDto attendanceRegistrationDto) {
        studentFeign.checkStudentExistByStudentId(attendanceRegistrationDto.getStudentId());
        log.info("Attempting to register attendance for Student ID: {} in Course ID: {}", attendanceRegistrationDto.getStudentId(), attendanceRegistrationDto.getCourseId());
        courseFeign.checkCourseExistByCourseId(attendanceRegistrationDto.getCourseId());
        studentCourseEnrollmentFeign.checkStudentExistInCourse(attendanceRegistrationDto.getStudentId(), attendanceRegistrationDto.getCourseId());
        Attendance attendance = DtoMapper.attendanceDtoSeparator(attendanceRegistrationDto);
        attendance.setStudentId(attendanceRegistrationDto.getStudentId());
        attendance.setCourseId(attendanceRegistrationDto.getCourseId());
        attendanceRepository.save(attendance);
        log.info("Attendance successfully recorded for Student ID: {} in Course ID: {}", attendanceRegistrationDto.getStudentId(), attendanceRegistrationDto.getCourseId());
        return "Attendance recorded successFully!";
    }

    @Override
    public List<CourseAttendanceProjection> findAttendanceByCourse(Long studentId) {

        studentFeign.checkStudentExistByStudentId(studentId);
        log.info("Fetching attendance report for Student ID: {}", studentId);
        List<Long> studentCourseRegisteredList = studentCourseEnrollmentFeign.getCoursesListByStudentId(studentId);
        List<CourseAttendanceProjection> courseAttendanceProjections = new ArrayList<>();
        for(Long courseId: studentCourseRegisteredList){
            log.debug("Student ID {} is registered in Course ID {}", studentId, courseId);
            CourseAttendanceProjection courseAttendanceProjection = new CourseAttendanceProjection();
            courseAttendanceProjection.setCourseId(courseId);
            String courseName = courseFeign.findCourseTitleByCourseId(courseId);
            courseAttendanceProjection.setCourseTitle(courseName);
            courseAttendanceProjection.setAttendancePercentage(0.0);
            courseAttendanceProjections.add(courseAttendanceProjection);
        }
        log.debug("Found {} courses for student {}", courseAttendanceProjections.size(), studentId);
        for(CourseAttendanceProjection course: courseAttendanceProjections){
            Long totalAttendedDays = attendanceRepository.countAttendanceByIdAndStudentId(course.getCourseId(),studentId);
            if(totalAttendedDays >0L){
                LocalDateTime firstAttendanceDate = attendanceRepository.findFirstEnrollmentDate(course.getCourseId(),studentId);
                LocalDateTime lastAttendanceDate = LocalDateTime.now();
                Long daysBetween = DateUtils.getCalendarDaysBetween(firstAttendanceDate,lastAttendanceDate);
                double attendancePercentage = AttendanceCalculator.calculateAttendance(totalAttendedDays,daysBetween);
                course.setAttendancePercentage(attendancePercentage);
                log.info("Course: {} | Attended: {} days | first Attended date: {} | last attended date: {} | Window: {} days | Rate: {}%",course.getCourseId(), totalAttendedDays, firstAttendanceDate,lastAttendanceDate, daysBetween, String.format("%.2f", attendancePercentage));
            }else{
                log.debug("Skipping Course {}: Zero attendance records found.", course.getCourseId());
            }
        }
        log.info("Completed attendance report for Student {}. Courses processed: {}", studentId, courseAttendanceProjections.size());
        return courseAttendanceProjections;
    }
}
