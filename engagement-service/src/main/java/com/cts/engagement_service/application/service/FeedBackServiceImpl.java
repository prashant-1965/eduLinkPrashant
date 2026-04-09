package com.cts.engagement_service.application.service;

import com.cts.classexception.FeedbackException;
import com.cts.dto.request.FeedbackDto;
import com.cts.dto.response.FeedbackProjection;
import com.cts.engagement_service.application.entity.FeedBack;
import com.cts.engagement_service.application.feign.FacultyFeign;
import com.cts.engagement_service.application.feign.StudentFeign;
import com.cts.engagement_service.application.repository.FeedBackRepository;
import com.cts.engagement_service.application.util.DtoMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class FeedBackServiceImpl implements IFeedBackService {

    private final FeedBackRepository feedBackRepository;
    private final StudentFeign studentFeign;
    private final FacultyFeign facultyFeign;


    @Override
    @Transactional
    public String registerFeedback(FeedbackDto feedbackDto) throws FeedbackException {
        log.info("Attempting to register feedback for User ID: {} with Reviewer Type: {}",
                feedbackDto.getAppUserId(), feedbackDto.getReviewerType());
        if(feedbackDto.getReviewerType().equalsIgnoreCase("STUDENT")){
            studentFeign.checkStudentExistByStudentId(feedbackDto.getAppUserId());
        } else if(feedbackDto.getReviewerType().equalsIgnoreCase("FACULTY")){
            facultyFeign.checkFacultyExistByFacultyId(feedbackDto.getAppUserId());
        }else{
            throw new FeedbackException("Invalid feedback type", HttpStatus.BAD_REQUEST);
        }
        FeedBack feedBack = DtoMapper.feedBackDtoSeparator(feedbackDto);
        feedBackRepository.save(feedBack);
        log.info("Feedback successfully saved for User ID: {}", feedbackDto.getAppUserId());
        return "Thank you for your feedback!";
    }

    @Override
    public List<FeedbackProjection> findFeedBackList() throws FeedbackException {
        log.info("Fetching all feedback records from the repository");
        List<FeedbackProjection> feedbackProjections = feedBackRepository.findFeedBackList();
        if(feedbackProjections.isEmpty()){
            log.warn("No feedback records found in the database");
            throw new FeedbackException("No feedback yet for the platform",HttpStatus.NOT_FOUND);
        }
        log.info("Successfully retrieved {} feedback records", feedbackProjections.size());
        return feedbackProjections;
    }
}
