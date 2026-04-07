package com.cts.classexception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FacultyCourseEnrollmentException extends RuntimeException {

    private final HttpStatus httpStatus;
    public FacultyCourseEnrollmentException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }
}
