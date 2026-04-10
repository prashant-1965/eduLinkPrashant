package com.cts.faculty_service.application.service;

import com.cts.classexception.FacultyException;
import com.cts.dto.request.AppUserRegistrationDto;
import com.cts.dto.request.FacultyRegistrationDto;
import com.cts.dto.response.AppUserDetailByIdDto;
import com.cts.dto.response.FacultyDetailByIdDto;
import com.cts.dto.response.FacultyDetailProjection;
import com.cts.faculty_service.application.entity.Faculty;
import com.cts.faculty_service.application.feign.AppUserFeign;
import com.cts.faculty_service.application.feign.CourseFeign;
import com.cts.faculty_service.application.projection.FacultyDetail;
import com.cts.faculty_service.application.repository.FacultyRepository;
import com.cts.faculty_service.application.util.DtoMapper;
import com.cts.util.RatingCalculator;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cts.dto.response.CourseProjection;


import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class FacultyServiceImpl implements IFacultyService{

    private final FacultyRepository facultyRepository;
    private final AppUserFeign appUserFeign;
    private final CourseFeign courseFeign;

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
    @Transactional
    public String updateFacultyRating(Long facultyId, double newFacultyRating) {
        log.info("Updating rating for Faculty ID: {} with new score: {}", facultyId, newFacultyRating);
        Optional<Faculty> faculty = facultyRepository.findFacultyById(facultyId);
        if(faculty.isEmpty()){
            log.error("Faculty with ID {} not found", facultyId);
            throw new FacultyException("Faculty is not registered",HttpStatus.NOT_FOUND);
        }
        Long totalFacultyRating = faculty.get().getTotalFacultyRatingCount();
        double newRating = RatingCalculator.calculateRating(faculty.get().getFacultyRating(),newFacultyRating,totalFacultyRating);
        faculty.get().setFacultyRating(newRating);
        faculty.get().setTotalFacultyRatingCount(totalFacultyRating+1);
        log.info("Update successful for Faculty ID: {}. Rating changed to {} (Total reviews: {})",facultyId, newRating, totalFacultyRating + 1);
        return "Thanks for you feedBack!";
    }

    @Override
    @Transactional
    public String deleteFaculty(Long facultyId) {
        log.info("Deletion request initiated for Faculty ID: {}", facultyId);
        Faculty faculty = facultyRepository.findFacultyById(facultyId)
                .orElseThrow(() -> new FacultyException("Faculty not found with ID: " + facultyId, org.springframework.http.HttpStatus.NOT_FOUND));
        facultyRepository.delete(faculty);
        log.info("Faculty ID: {} and associated user deleted successfully", facultyId);
        return "Faculty record deleted successfully!";
    }

    @Override
    public String getFacultyNameByFacultyId(Long facultyId) {
        Long appUserId = facultyRepository.findAppUserIdByFacultyId(facultyId);
        return appUserFeign.findAppUserNameByAppUserId(appUserId);
    }

    @Override
    public List<CourseProjection> getFacultyCourses(Long facultyId) {
        log.debug("Fetching courses for faculty: {}", facultyId);
        this.checkFacultyExistByFacultyId(facultyId);
        return courseFeign.getCoursesByFaculty(facultyId).getBody();
    }

    @Override
    public void checkFacultyExistByFacultyId(Long facultyId) throws FacultyException {
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

//    public List<Exam> getupComingExams(Long facultyId ){
//        return facultyRepository.findUpcomingExamsByFacultyId(facultyId);
//    }
//
//    public int getupComingExamsCount(Long facultyId){
//        return facultyRepository.getUpcomingExamsCount(facultyId);
//    }

    public Optional<FacultyDetailByIdDto> getFacultyProfile(Long facultyId) throws FacultyException {
        Optional<Faculty> faculty = facultyRepository.findFacultyById(facultyId);
        if(faculty.isEmpty()){
            log.error("Faculty not found for Id: {}", facultyId);
            throw new FacultyException("Faculty not found for ID: " + facultyId, HttpStatus.NOT_FOUND);
        }
        AppUserDetailByIdDto appUserDetailByIdDto = appUserFeign.findAppUserDetailsByAppUserId(faculty.get().getAppUserId());
        return  Optional.of(DtoMapper.facultyAppUserMapper(faculty.get(),appUserDetailByIdDto));
    }

    public String registerFallback(FacultyRegistrationDto facultyRegistrationDto, Throwable t) {
        log.error("Fallback triggered for user: {}. Reason: {}",
                facultyRegistrationDto.getUserEmail(), t.getMessage());
        return "Registration service is currently experiencing high traffic. " +
                "Please try again in a few moments. Error: " + t.getLocalizedMessage();
    }
}
