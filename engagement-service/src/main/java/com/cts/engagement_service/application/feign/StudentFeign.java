package com.cts.engagement_service.application.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "student-service")
public interface StudentFeign {

    @GetMapping("/studnet/checkStudentExistByStudentId/{studentId}")
    void checkStudentExistByStudentId(@PathVariable Long studentId);
}
