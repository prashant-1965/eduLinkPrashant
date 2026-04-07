package com.cts.faculty_service.application.service;

import com.cts.classexception.FacultyException;
import com.cts.dto.request.AppUserRegistrationDto;
import com.cts.dto.request.FacultyRegistrationDto;
import com.cts.dto.response.FacultyDetailProjection;
import com.cts.faculty_service.application.entity.Faculty;
import com.cts.faculty_service.application.feign.AppUserFeign;
import com.cts.faculty_service.application.projection.FacultyDetail;
import com.cts.faculty_service.application.repository.FacultyRepository;
import com.cts.faculty_service.application.util.DtoMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @Override
    public void checkFacultyByFacultyId(Long facultyId) throws FacultyException {
        if (facultyRepository.findFacultyById(facultyId).isEmpty()) {
            log.error("Faculty verification failed for ID: {}", facultyId);
            throw new FacultyException(facultyId + " is not registered", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public FacultyDetailProjection getFacultyDetailsByFacultyId(Long facultyId)throws FacultyException {
        Optional<Faculty> faculty = facultyRepository.findFacultyById(facultyId);
        if(faculty.isEmpty()){
            log.error("Faculty not found for ID: {}", facultyId);
            throw new FacultyException("Faculty not found for ID: " + facultyId, HttpStatus.NOT_FOUND);
        }
        String facultyName = appUserFeign.findAppUserNameByAppUserId(faculty.get().getAppUserId());
        Optional<FacultyDetail> facultyDetail = facultyRepository.findFacultyDetailProjectionByFacultyId(facultyId);
        if(facultyDetail.isEmpty()){
            log.error("Faculty details not found for ID: {}", facultyId);
            throw new FacultyException("Faculty details not found for ID: " + facultyId, HttpStatus.NOT_FOUND);
        }
        FacultyDetailProjection facultyDetailProjection = new FacultyDetailProjection(facultyName, facultyDetail.get().getFacultyRating(), facultyDetail.get().getFacultyYearOfExperience());
        log.info("Successfully retrieved faculty details for ID: {}", facultyId);
        return facultyDetailProjection;
    }

    public String registerFallback(FacultyRegistrationDto facultyRegistrationDto, Throwable t) {
        log.error("Fallback triggered for user: {}. Reason: {}",
                facultyRegistrationDto.getUserEmail(), t.getMessage());
        return "Registration service is currently experiencing high traffic. " +
                "Please try again in a few moments. Error: " + t.getLocalizedMessage();
    }
}
