package com.cts.course_service.application.service;

import com.cts.classexception.CourseException;
import com.cts.course_service.application.feign.FacultyFeign;
import com.cts.course_service.application.feign.StudentFeign;
import com.cts.dto.response.CourseDetailByIdProjection;
import com.cts.course_service.application.projection.CourseDetailProjection;
import com.cts.dto.response.CourseProjection;
import com.cts.course_service.application.util.DtoMapper;
import com.cts.course_service.application.entity.Course;
import com.cts.course_service.application.feign.CourseEnrollmentFeign;
import com.cts.course_service.application.repository.CourseRepository;
import com.cts.dto.request.CourseEnrollmentDto;
import com.cts.dto.request.CourseRegistrationDto;
import com.cts.dto.response.FacultyDetailProjection;
import com.cts.util.RatingCalculator;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CourseServiceImpl implements ICourseService {

    private final CourseRepository courseRepository;
    private final CourseEnrollmentFeign courseEnrollmentFeign;
    private final FacultyFeign facultyFeign;
    private final StudentFeign studentFeign;


    @Override
    @Transactional
    @CircuitBreaker(name = "courseRegister", fallbackMethod = "fallbackRegisterCourse")
    @Retry(name = "courseRegister")
    public String registerCourse(CourseRegistrationDto courseRegistrationDto) {
        log.info("Course registration has intercepted inside service");
        facultyFeign.checkFacultyByFacultyId(courseRegistrationDto.getFacultyId());
        Course course = DtoMapper.courseDtoSeparator(courseRegistrationDto);
        log.error("Unable to separate faculty from courseRegistrationDto");
        course.setCourseStatus("ACTIVE");
        courseEnrollmentFeign.assignCourseToFaculty(courseRegistrationDto.getFacultyId(), course.getCourseId());
        courseRepository.save(course);
        log.info("Course with id {} saved successFully into database", course.getCourseId());
        return "Course has registered successFully with course Id: " + course.getCourseId();
    }

    @Override
    public void checkCourseExistByCourseId(Long courseId) {
        log.info("Checking existence of course with ID: {}", courseId);
        boolean exists = courseRepository.existsByCourseId(courseId);
        if (!exists) {
            log.warn("Course with ID {} does not exist", courseId);
            throw new CourseException("Course is not registered with id: " + courseId, HttpStatus.NOT_FOUND);
        }
        log.info("Course with ID {} exists", courseId);
    }

    @Override
    public String findCourseTitleByCourseId(Long courseId) {
        this.checkCourseExistByCourseId(courseId);
        String courseName = courseRepository.findCourseTitleByCourseId(courseId);
        log.info("Course name for course ID {} is '{}'", courseId, courseName);
        return courseName;
    }

    @Override
    @Transactional
    public String updateCourse(Long courseId, CourseRegistrationDto courseRegistrationDto) {
        log.info("Updating course details for Course ID: {}", courseId);
        Course existingCourse = courseRepository.findCourseById(courseId)
                .orElseThrow(() -> new CourseException("Course not found with ID: " + courseId, HttpStatus.NOT_FOUND));
        DtoMapper.updateCourseFromDto(existingCourse, courseRegistrationDto);
        courseRepository.save(existingCourse);
        log.info("Course Id: {} updated successfully", courseId);
        return "Course updated successfully!";
    }

    @Override
    @Transactional
    public String patchCourse(Long courseId, Map<String, Object> updates) throws CourseException{
        log.info("Patch update initiated for Course ID: {}", courseId);
        Course course = courseRepository.findCourseById(courseId)
                .orElseThrow(() -> new CourseException("Course not found with ID: " + courseId, HttpStatus.NOT_FOUND));
        updates.forEach((key, value) -> {
            if (value != null) {
                switch (key) {
                    case "courseTitle":
                        course.setCourseTitle(value.toString());
                        break;
                    case "courseSubject":
                        course.setCourseSubject(value.toString());
                        break;
                    case "courseGradeLevel":
                        course.setCourseGradeLevel(value.toString());
                        break;
                    case "courseCredit":
                        course.setCourseCredit(Integer.parseInt(value.toString()));
                        break;
                    case "courseStatus":
                        course.setCourseStatus(value.toString());
                        break;
                }
            }
        });
        courseRepository.save(course);
        log.info("Course ID: {} partially updated successfully", courseId);
        return "Course partially updated successfully!";
    }

    @Override
    @Transactional
    public String deleteCourse(Long courseId) {
        log.info("Deletion request initiated for Course ID: {}", courseId);
        Course course = courseRepository.findCourseById(courseId)
                .orElseThrow(() -> new CourseException("Course not found with ID: " + courseId, HttpStatus.NOT_FOUND));
        course.setCourseStatus("INACTIVE");
        log.info("Course Id: {} deleted successfully", courseId);
        return "Course deleted successfully with Id : "+courseId;
    }

    @Override
    public List<CourseProjection> findAllAvailableCourse() throws CourseException {
        log.info("User has requested to display course List!");
        List<CourseProjection> courseProjections = courseRepository.findAllAvailableCourse();
        if (courseProjections.isEmpty()) {
            log.error("no course is available to display");
            throw new CourseException("No course Available!", HttpStatus.NOT_FOUND);
        }
        log.info("Course List has been accessed SuccessFully and first course name is {}", courseProjections.getFirst().getCourseTitle());
        return courseProjections;
    }

    @Override
    public CourseDetailByIdProjection findCourseDetailsById(Long courseId) throws CourseException {
        log.info("Fetching details for courseId: {}", courseId);
        log.debug("Calling faculty-course-enrollment-feign to get facultyId for courseId: {}", courseId);
        Long facultyId = courseEnrollmentFeign.findFacultyIdByCourseId(courseId);
        log.debug("Calling faculty-feign to get details for facultyId: {}", facultyId);
        FacultyDetailProjection facultyDetailProjection = facultyFeign.getFacultyDetailsByFacultyId(facultyId);
        log.debug("Querying course repository for courseId: {}", courseId);
        Optional<CourseProjection> courseProjection = courseRepository.findByCourseId(courseId);
        if(courseProjection.isEmpty()){
            log.error("Course lookup failed: CourseId {} not found in database", courseId);
            throw new CourseException("Course is not registered", HttpStatus.NOT_FOUND);
        }
        CourseDetailByIdProjection courseDetailByIdProjection = DtoMapper.courseDetailsByIdGenerator(facultyDetailProjection,courseProjection.get());
        log.info("Successfully retrieved and mapped details for courseId: {}", courseId);
        return courseDetailByIdProjection;
    }

    @Transactional
    public String courseEnrollmentRequest(CourseEnrollmentDto courseEnrollmentDto) throws CourseException {
        log.info("Received enrollment request: Student ID {} for Course ID {}", courseEnrollmentDto.getStudentId(), courseEnrollmentDto.getCourseId());
        studentFeign.checkStudentExistByStudentId(courseEnrollmentDto.getStudentId());
        Optional<Course> course = courseRepository.findCourseById(courseEnrollmentDto.getCourseId());
        if (course.isEmpty()) {
            log.error("Enrollment failed: Course ID {} not found", courseEnrollmentDto.getCourseId());
            throw new CourseException("Invalid course id: " + courseEnrollmentDto.getCourseId(), HttpStatus.BAD_REQUEST);
        }
        courseEnrollmentFeign.assignCourseToStudent(courseEnrollmentDto.getStudentId(), courseEnrollmentDto.getCourseId());
        log.info("Successfully enrolled Student ID {} into Course ID {}", courseEnrollmentDto.getStudentId(), courseEnrollmentDto.getCourseId());
        return "Enrolled SuccessFull!";
    }

    @Override
    @Transactional
    public String updateCourseRating(Long courseId, double newCourseRating) throws CourseException {
        log.info("Updating rating for course ID: {} with new rating: {}", courseId, newCourseRating);
        Optional<Course> course = courseRepository.findCourseById(courseId);
        if (course.isEmpty()) {
            log.error("Course not found with ID: {}", courseId);
            throw new CourseException("Course is not registered", HttpStatus.NOT_FOUND);
        }
        double newRating = RatingCalculator.calculateRating(course.get().getCourseRating(), newCourseRating, course.get().getTotalCourseRatingCount());
        course.get().setTotalCourseRatingCount(course.get().getTotalCourseRatingCount() + 1);
        course.get().setCourseRating(newRating);
        log.info("Course {} updated. New Rating: {}, Total Reviews: {}",
                courseId, newRating, course.get().getTotalCourseRatingCount());
        return "Thanks for you feedBack!";
    }

    @Override
    public List<CourseDetailProjection> findCourseListByStudentId(Long studentId) throws CourseException {
        log.info("Fetching course list for student ID: {}", studentId);

        studentFeign.checkStudentExistByStudentId(studentId);
        List<Long> courseIdList = courseEnrollmentFeign.getCoursesListByStudentId(studentId);
        List<CourseDetailProjection> courseDetailProjections = new ArrayList<>();
        for(Long courseId : courseIdList){
            Optional<CourseDetailProjection> courseDetail = courseRepository.findCourseListByCourseId(courseId);
            if(courseDetail.isEmpty()){
                log.warn("Course details not found for course ID: {}", courseId);
            }else {
                log.info("Course details found for course ID: {}", courseId);
                courseDetailProjections.add(courseDetail.get());
            }
        }
        if (courseDetailProjections.isEmpty()) {
            log.warn("No courses found for student ID: {}", studentId);
            throw new CourseException("No course available for student id: " + studentId, HttpStatus.NOT_FOUND);
        }
        log.info("Successfully retrieved {} courses for student ID: {}", courseDetailProjections.size(), studentId);
        return courseDetailProjections;
    }

    @Override
    public List<CourseProjection> getCoursesByFaculty(Long facultyId) {
        List<Long> courseIdList = courseEnrollmentFeign.getCoursesListByFacultyId(facultyId);
        List<CourseProjection> courseProjection = new ArrayList<>();
        for (Long courseId : courseIdList) {
            Optional<CourseProjection> course = courseRepository.findByCourseId(courseId);
            if (course.isEmpty()) {
                log.error("Course with ID {} not found for faculty ID {}", courseId, facultyId);
            }else{
                log.info("Course with ID {} found for faculty ID {}", courseId, facultyId);
                courseProjection.add(course.get());
            }
        }
        return courseProjection;
    }

    public int getFacultyCourseCount(Long facultyId) {
        int count = courseEnrollmentFeign.getFacultyCourseCount(facultyId);
        log.info("Faculty ID {} is assigned to {} courses", facultyId, count);
        return count;
    }

    public String fallbackRegisterCourse(CourseRegistrationDto dto, Throwable t) {
        log.error("Fallback triggered for course '{}'. Reason: {}", dto.getCourseTitle(), t.getMessage());
        return "Registration is temporarily unavailable. Please try again later. Error: " + t.getMessage();
    }
}
