package com.cts.faculty_service.application.service;

import com.cts.dto.request.AppUserRegistrationDto;
import com.cts.dto.request.FacultyRegistrationDto;
import com.cts.faculty_service.application.entity.Faculty;
import com.cts.faculty_service.application.feign.AppUserFeign;
import com.cts.faculty_service.application.repository.FacultyRepository;
import com.cts.faculty_service.application.util.DtoMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class FacultyServiceImpl implements IFacultyService{

    private final FacultyRepository facultyRepository;
    private final AppUserFeign appUserFeign;

    @Override
    @Transactional
    @Retry(name = "registerFaculty", fallbackMethod = "registerFallback")
    @CircuitBreaker(name = "registerFaculty", fallbackMethod = "registerFallback")
    public String registerFaculty(FacultyRegistrationDto facultyRegistrationDto) {
        log.info("Initiating faculty registration for user: {}", facultyRegistrationDto.getUserEmail());
        Faculty faculty = DtoMapper.facultyDtoSeparator(facultyRegistrationDto);
        AppUserRegistrationDto appUserDto = AppUserRegistrationDto.from(facultyRegistrationDto, "FACULTY");
        ResponseEntity<Long> appUserId = appUserFeign.appUserRegistration(appUserDto);
        faculty.setAppUserId(appUserId.getBody());
        facultyRepository.save(faculty);
        log.info("Successfully registered faculty. Assigned Faculty ID: {}", faculty.getFacultyId());
        return "Thanks for Registration, Your User Id is: "+faculty.getFacultyId();
    }

    public String registerFallback(FacultyRegistrationDto facultyRegistrationDto, Throwable t) {
        log.error("Fallback triggered for user: {}. Reason: {}",
                facultyRegistrationDto.getUserEmail(), t.getMessage());
        return "Registration service is currently experiencing high traffic. " +
                "Please try again in a few moments. Error: " + t.getLocalizedMessage();
    }
}
