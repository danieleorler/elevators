package com.tingco.codechallenge.elevator.config;

import com.tingco.codechallenge.elevator.api.exceptions.ElevatorNotFoundException;
import com.tingco.codechallenge.elevator.api.exceptions.NoElevatorAvailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionMapping extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { ElevatorNotFoundException.class, NoElevatorAvailableException.class })
    protected ResponseEntity<String> elevatorMissing(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}
