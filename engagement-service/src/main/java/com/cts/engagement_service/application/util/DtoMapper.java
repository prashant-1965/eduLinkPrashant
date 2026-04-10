package com.cts.engagement_service.application.util;

import com.cts.dto.request.FeedbackDto;
import com.cts.engagement_service.application.entity.FeedBack;

public class DtoMapper {
    public static FeedBack feedBackDtoSeparator(FeedbackDto feedbackDto){
        FeedBack feedBack = new FeedBack();
        feedBack.setMessage(feedbackDto.getComment());
        feedBack.setRating(feedbackDto.getRating());
        feedBack.setAppUserRoleId(feedbackDto.getAppUserRoleId());
        return feedBack;
    }
}
