package com.exozet.gitresumeweb.exception;

import javax.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MvcExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public void badRequest(ConstraintViolationException ex){

    }

    @ExceptionHandler(UserNotFoundException.class)
    public void badRequest(UserNotFoundException ex){

    }


}
