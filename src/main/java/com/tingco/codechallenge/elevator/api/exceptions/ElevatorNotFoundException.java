package com.tingco.codechallenge.elevator.api.exceptions;

/**
 * @author daniele.orler
 */
public class ElevatorNotFoundException extends RuntimeException {
    public ElevatorNotFoundException(String message) {
        super(message);
    }
}
