package com.cts.engagement_service.application.repository;

import com.cts.dto.response.FeedbackProjection;
import com.cts.engagement_service.application.entity.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedBackRepository extends JpaRepository<FeedBack,Long> {
    @Query("select  new com.cts.dto.response.FeedbackProjection(f.appUserName, f.message, f.rating) from FeedBack f")
    List<FeedbackProjection> findFeedBackList();
}
