package com.cts.engagement_service.application.controller;

import com.cts.dto.request.FeedbackDto;
import com.cts.dto.response.FeedbackProjection;
import com.cts.engagement_service.application.service.IFeedBackService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/feedback")
public class FeedBackController {

    private final IFeedBackService feedbackService;

    @PreAuthorize("hasAnyRole('STUDENT', 'FACULTY')")
    @PostMapping("/register")
    public ResponseEntity<String> registerFeedback(@Valid @RequestBody FeedbackDto feedbackDto) {
        log.info("Received POST request to register feedback for User ID: {}", feedbackDto.getAppUserRoleId());
        return ResponseEntity.status(200).body(feedbackService.registerFeedback(feedbackDto));
    }

    @GetMapping("/getFeedbackList")
    public ResponseEntity<List<FeedbackProjection>> findFeedBackList(){
        return ResponseEntity.status(200).body(feedbackService.findFeedBackList());
    }
}
