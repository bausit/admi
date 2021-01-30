package org.bausit.admin.controllers;

import lombok.extern.log4j.Log4j2;
import org.bausit.admin.exceptions.InvalidRequestException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j2
public class ControllerExceptionHandler {

    @ExceptionHandler(TypeMismatchException.class)
    public HttpStatus handleTypeMismatchException(TypeMismatchException e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid value: " + e.getValue(), e);
    }

    //more readable @valid error messages
    @ExceptionHandler(WebExchangeBindException.class)
    public HttpStatus handleWebExchangeBindException(WebExchangeBindException e) {
        throw new WebExchangeBindException(e.getMethodParameter(), e.getBindingResult()) {
            public String getMessage() {
                return getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            }
        };
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public HttpStatus handleEntityNotFoundException(EntityNotFoundException e) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public HttpStatus handleInvalidRequestException(InvalidRequestException e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
