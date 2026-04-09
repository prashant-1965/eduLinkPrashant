package com.cts.engagement_service.application.service;

import com.cts.dto.request.FeedbackDto;
import com.cts.dto.response.FeedbackProjection;

import java.util.List;

public interface IFeedBackService {
    String registerFeedback(FeedbackDto feedbackDto);
    List<FeedbackProjection> findFeedBackList();
}
