package com.tingco.codechallenge.elevator.api.exceptions;

public class NoElevatorAvailableException extends RuntimeException {
    public NoElevatorAvailableException(String message) {
        super(message);
    }
}
