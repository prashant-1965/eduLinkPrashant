package com.cts.classexception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppUserException extends  RuntimeException{
    private final HttpStatus httpStatus;

    public AppUserException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }
}
