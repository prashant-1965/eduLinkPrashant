package com.cts.classexception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FacultyException extends RuntimeException {
    public final HttpStatus httpStatus;
    public FacultyException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
