package com.cts.course_service.application.util;

import com.cts.course_service.application.entity.Course;
import com.cts.course_service.application.entity.LearningMaterial;
import com.cts.dto.request.LearningMaterialRegistrationDto;
import com.cts.dto.response.CourseProjection;
import com.cts.dto.request.CourseRegistrationDto;
import com.cts.dto.response.CourseDetailByIdProjection;
import com.cts.dto.response.FacultyDetailProjection;
import com.cts.util.UIDGeneratorUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class DtoMapper {
    public static Course courseDtoSeparator(CourseRegistrationDto courseRegistrationDto){
        Course course = new Course();
        course.setCourseTitle(courseRegistrationDto.getCourseTitle());
        course.setCourseSubject(courseRegistrationDto.getCourseSubject());
        course.setCourseCredit(courseRegistrationDto.getCourseCredit());
        course.setCourseRating(0.0);
        course.setTotalCourseRatingCount(0L);
        course.setCourseGradeLevel(courseRegistrationDto.getCourseGradeLevel());
        Long courseId = UIDGeneratorUtils.uidGenerator();
        course.setCourseId(courseId);
        return course;
    }
    public static void updateCourseFromDto(Course course, CourseRegistrationDto dto) {
        course.setCourseTitle(dto.getCourseTitle());
        course.setCourseSubject(dto.getCourseSubject());
        course.setCourseCredit(dto.getCourseCredit());
        course.setCourseGradeLevel(dto.getCourseGradeLevel());
        course.setCourseStatus("INACTIVE");
    }

    public static CourseDetailByIdProjection courseDetailsByIdGenerator(FacultyDetailProjection facultyDetailProjection , CourseProjection courseProjection){
        CourseDetailByIdProjection courseDetailByIdProjection = new CourseDetailByIdProjection();
        courseDetailByIdProjection.setCourseId(courseProjection.getCourseId());
        courseDetailByIdProjection.setCourseTitle(courseProjection.getCourseTitle());
        courseDetailByIdProjection.setCourseSubject(courseProjection.getCourseSubject());
        courseDetailByIdProjection.setCourseGradeLevel(courseProjection.getCourseGradeLevel());
        courseDetailByIdProjection.setCourseCredit(courseProjection.getCourseCredit());
        courseDetailByIdProjection.setCourseStatus(courseProjection.getCourseStatus());
        courseDetailByIdProjection.setCourseRating(courseProjection.getCourseRating());
        courseDetailByIdProjection.setFacultyName(facultyDetailProjection.getFacultyName());
        courseDetailByIdProjection.setFacultyRating(facultyDetailProjection.getFacultyRating());
        courseDetailByIdProjection.setFacultyYearOfExperience(facultyDetailProjection.getFacultyYearOfExperience());
        return courseDetailByIdProjection;
    }

    public static LearningMaterial learningMaterialDtoSeparator(LearningMaterialRegistrationDto dto) throws IOException {
        LearningMaterial learningMaterial = new LearningMaterial();
        learningMaterial.setLearningMaterialTitle(dto.getLearningMaterialTitle());

        MultipartFile file = dto.getLearningMaterialFile();
        if (file == null || file.isEmpty()) {
            throw new IOException("File is empty or missing");
        }
        // examLocalDateTime is usually set at creation, but you can update it here if needed

        File uploadDir = new File("uploads");
        if (!uploadDir.exists()) uploadDir.mkdirs();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File dest = new File(uploadDir.getAbsolutePath() + File.separator + fileName);
        file.transferTo(dest);
        learningMaterial.setLearningMaterialFile(dest.getPath());
        learningMaterial.setLearningMaterialUploadedDate(LocalDateTime.now());
        learningMaterial.setLearningMaterialStatus("UPLOADED");

        return learningMaterial;
    }

}
