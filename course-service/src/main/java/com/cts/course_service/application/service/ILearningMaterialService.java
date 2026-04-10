package com.cts.course_service.application.service;

import com.cts.course_service.application.projection.LearningCourseMaterialProjection;
import com.cts.dto.request.LearningMaterialRegistrationDto;
import org.springframework.core.io.Resource;

public interface ILearningMaterialService {
    String registerLearningMaterial(LearningMaterialRegistrationDto learningMaterialRegistrationDto);
    LearningCourseMaterialProjection findMaterialsByCourseId(Long courseId);
    Resource getFileFromProjection(Long id);
}
